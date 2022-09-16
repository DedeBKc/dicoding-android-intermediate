package com.dedebkc.intermediate.ui.mainmenu.home

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.dedebkc.intermediate.R
import com.dedebkc.intermediate.ui.adapter.ListStoryAdapter
import com.dedebkc.intermediate.ui.ViewModelFactory
import com.dedebkc.intermediate.databinding.FragmentHomeBinding
import com.dedebkc.intermediate.ui.adapter.LoadingStateListStoryAdapter
import com.dedebkc.intermediate.ui.mapmenu.MapActivity

class HomeFragment : Fragment() {
    private lateinit var factory: ViewModelFactory
    private val homeViewModel: HomeViewModel by activityViewModels { factory }
    private var _binding: FragmentHomeBinding? = null
    private lateinit var listStoryAdapter: ListStoryAdapter
    private var hideNavView = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mapMenu.setOnClickListener {
            Log.d("CLICK", "CLICK ME!!")
            startActivity(Intent(activity, MapActivity::class.java))
        }

        factory = ViewModelFactory.getInstance(requireActivity())

        binding.refreshLayout.isRefreshing = true
        binding.refreshLayout.setOnRefreshListener {
            listStoryAdapter.refresh()
        }
        fetchUserStories()

        initObserve()

        val navView = requireActivity().findViewById<View>(R.id.nav_view)
        if (navView != null) {
            hideShowBottomNavigation(navView)
        }

    }

    private fun hideShowBottomNavigation(navView: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.rvStory.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                val height = (navView.height + 32).toFloat()

                if (!hideNavView && scrollY > oldScrollY) {
                    hideNavView = true
                    ObjectAnimator.ofFloat(navView, "translationY", 0f, height).apply {
                        duration = 200
                        start()
                    }
                }

                if (hideNavView && scrollY < oldScrollY) {
                    hideNavView = false
                    ObjectAnimator.ofFloat(navView, "translationY", height, 0f).apply {
                        duration = 200
                        start()
                    }
                }
            }
        }
    }

    private fun fetchUserStories() {
        homeViewModel.getUserToken().observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = true
            homeViewModel.getUserStories(it)
            initRecycler()
            Log.e("Home", "Token: $it")
        }
    }

    private fun initObserve() {
        homeViewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRecycler() {
        binding.rvStory.layoutManager = LinearLayoutManager(activity)
        listStoryAdapter = ListStoryAdapter()
        homeViewModel.userStories.observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = false
            listStoryAdapter.submitData(lifecycle, it)
        }
        binding.rvStory.adapter = listStoryAdapter.withLoadStateFooter(
            footer = LoadingStateListStoryAdapter { listStoryAdapter.retry() }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        listStoryAdapter.submitData(lifecycle, PagingData.empty())
    }
}