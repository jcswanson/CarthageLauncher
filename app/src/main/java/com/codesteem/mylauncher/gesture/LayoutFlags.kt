package com.codesteem.mylauncher.gesture

import androidx.recyclerview.widget.*

/**
 * Enum with predefined gesture flags for various layout managers, see [RecyclerView.LayoutManager].
 * This enum provides drag and swipe flags for [LinearLayoutManager], [GridLayoutManager],
 * and [StaggeredGridLayoutManager].
 *
 * @author thesurix
 */
internal enum class LayoutFlags {
    /**
     * Flags for [LinearLayoutManager].
     */
    LINEAR {
        override fun getDragFlags(layout: RecyclerView.LayoutManager?): Int {
            val linearLayout = layout as? LinearLayoutManager ?: return 0
            return when (linearLayout.orientation) {
                LinearLayoutManager.HORIZONTAL -> ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                else -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
            }
        }

        override fun getSwipeFlags(layout: RecyclerView.LayoutManager?): Int {
            val linearLayout = layout as? LinearLayoutManager ?: return 0
            return when (linearLayout.orientation) {
                LinearLayoutManager.HORIZONTAL -> ItemTouchHelper.UP
                else -> ItemTouchHelper.RIGHT
            }
        }
    },

    /**
     * Flags for [GridLayoutManager].
     */
    GRID {
        override fun getDragFlags(layout: RecyclerView.LayoutManager?): Int {
            return ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        }

        override fun getSwipeFlags(layout: RecyclerView.LayoutManager?): Int {
            val gridLayout = layout as? GridLayoutManager ?: return 0
            return when (gridLayout.orientation) {
                GridLayoutManager.HORIZONTAL -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
                else -> ItemTouchHelper.RIGHT
            }
        }
    },

    /**
     * Flags for [StaggeredGridLayoutManager].
     */
    STAGGERED {
        override fun getDragFlags(layout: RecyclerView.LayoutManager?): Int {
            return ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        }

        override fun getSwipeFlags(layout: RecyclerView.LayoutManager?): Int {
            val staggeredGridLayout = layout as? StaggeredGridLayoutManager ?: return 0
            return when (staggeredGridLayout.orientation) {
                StaggeredGridLayoutManager.HORIZONTAL -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
                StaggeredGridLayoutManager.VERTICAL -> ItemTouchHelper.RIGHT
                else -> 0
            }
        }
    };

    /**
     * Returns drag flags for the given layout manager.
     *
     * @param layout layout manager instance
     * @return drag flags
     */
    internal abstract fun getDragFlags(layout: RecyclerView.LayoutManager?): Int

    /**
     * Returns swipe flags for the given layout manager.
     *
     * @param layout layout manager instance
     * @return swipe flags
     */
    internal abstract fun getSwipeFlags(layout: RecyclerView.LayoutManager?): Int
}
