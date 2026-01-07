package com.airss.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.airss.AppGraph
import com.airss.R
import com.airss.data.Repository
import com.airss.domain.Summarizer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private lateinit var repo: Repository
    private lateinit var vm: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repo = AppGraph.repository(requireActivity().application)
        vm = HomeViewModel(repo)

        val listView = view.findViewById<ListView>(R.id.list)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

        toolbar.setOnClickListener {
            vm.refresh()
        }

        fab.setOnClickListener {
            // TODO: 合并播报入口（导航到 PlayerFragment 并传入队列）
        }

        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                // 首次冷启动拉取一次
                repo.refreshAll()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repo.stream().collectLatest { list ->
                val titles = list.map { it.source + " · " + it.title + "\n" + it.summary }
                listView.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, titles)
            }
        }
    }
}
