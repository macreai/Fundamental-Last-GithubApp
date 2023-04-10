package com.macreai.githubapp.ui.detail.followerfollowing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.macreai.githubapp.R
import com.macreai.githubapp.data.remote.response.ItemsItem
import com.macreai.githubapp.databinding.FragmentFollowerFollowingBinding
import com.macreai.githubapp.ui.UserAdapter
import com.macreai.githubapp.ui.detail.DetailActivity

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowerFollowingBinding? = null
    private val binding get() = _binding
    private val followingViewModel by viewModels<FollowingViewModel>()
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = arguments?.getString(DetailActivity.EXTRA_USERNAME).toString()
        _binding = FragmentFollowerFollowingBinding.bind(view)

        followingViewModel.setListFollowing(username)
        followingViewModel.listFollowing.observe(viewLifecycleOwner, {
            showRecyclerView(it)
        })

        followingViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showRecyclerView(user: List<ItemsItem>) {

        adapter = UserAdapter(user)
        adapter.setData(user)
        binding?.apply {
            rvUser.layoutManager = LinearLayoutManager(activity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter
        }

        //item click
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ItemsItem) {
                Toast.makeText(requireActivity(), "Following user", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) binding?.githubLoading?.visibility = View.VISIBLE
        else binding?.githubLoading?.visibility = View.GONE
    }
}