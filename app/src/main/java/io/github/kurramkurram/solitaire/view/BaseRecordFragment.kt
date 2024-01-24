package io.github.kurramkurram.solitaire.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.github.kurramkurram.solitaire.databinding.FragmentBaseRecordBinding

/**
 * 記録画面のベース.
 */
@AndroidEntryPoint
class BaseRecordFragment : Fragment() {

    private lateinit var binding: FragmentBaseRecordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = ViewPagerAdapter(parentFragmentManager, lifecycle)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = pagerAdapter.getTabTitle(position)
        }.attach()
    }

    companion object {
        const val TAG = "BaseRecordFragment"
    }

    internal class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fm, lifecycle) {

        private val contents = listOf(RecordFragment(), AnalysisFragment(), GraphFragment())
        private val title = listOf("移動回数", "統計", "グラフ")
        fun getTabTitle(position: Int): CharSequence {
            val index = if (title.size > position) {
                position
            } else {
                0
            }
            return title[index]
        }

        override fun getItemCount(): Int = contents.size

        override fun createFragment(position: Int): Fragment {
            val index = if (contents.size > position) {
                position
            } else {
                0
            }
            return contents[index]
        }
    }
}
