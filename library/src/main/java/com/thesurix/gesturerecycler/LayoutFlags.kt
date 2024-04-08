package com.thesurix.gesturerecycler

import androidx.recyclerview.widget.*

/**
 * Enum with predefined gesture flags for various layout managers, see [RecyclerView.LayoutManager]
 * This enum provides drag and swipe flags for [LinearLayoutManager], [GridLayoutManager],
 * and [StaggeredGridLayoutManager].
 *
 * @author thesurix
 */
internal enum class LayoutFlags {
    LINEAR {
        /**
         * Returns drag flags for a horizontal or vertical [LinearLayoutManager].
         * 
         * @param layout layout manager instance
         * @return drag flags
         */
        override fun getDragFlags(layout: RecyclerView.LayoutManager): Int {
            val linearLayout = layout as LinearLayoutManager
            return when (linearLayout.orientation) {
                LinearLayoutManager.HORIZONTAL -> ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                else -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
            }
        }

        /**
         * Returns swipe flags for a horizontal or vertical [LinearLayoutManager].
         * 
         * @param layout layout manager instance
         * @return swipe flags
         */
        override fun getSwipeFlags(layout: RecyclerView.LayoutManager): Int {
            val linearLayout = layout as LinearLayoutManager
            return when (linearLayout.orientation) {
                LinearLayoutManager.HORIZONTAL -> ItemTouchHelper.UP
                else -> ItemTouchHelper.RIGHT
            }
        }
    },
    GRID {
        /**
         * Returns drag flags for a [GridLayoutManager] with any orientation.
         *
         * @param layout layout manager instance
         * @return drag flags
         */
        override fun getDragFlags(layout: RecyclerView.LayoutManager): Int {
            return ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        }

        /**
         * Returns swipe flags for a [GridLayoutManager] with any orientation.
         *
         * @param layout layout manager instance
         * @return swipe flags
         */
        override fun getSwipeFlags(layout: RecyclerView.LayoutManager): Int {
            val gridLayout = layout as GridLayoutManager
            return when (gridLayout.orientation) {
                GridLayoutManager.HORIZONTAL -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
                else -> ItemTouchHelper.RIGHT
            }
        }
    },
    STAGGERED {
        /**
         * Returns drag flags for a horizontal or vertical [StaggeredGridLayoutManager].
         *
         * @param layout layout manager instance
         * @return drag flags
         */
        override fun getDragFlags(layout: RecyclerView.LayoutManager): Int {
            return ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        }

        /**
         * Returns swipe flags for a horizontal or vertical [StaggeredGridLayoutManager].
         *
         * @param layout layout manager instance
         * @return swipe flags
         */
        override fun getSwipeFlags(layout: RecyclerView.LayoutManager): Int {
            val staggeredGridLayout = layout as StaggeredGridLayoutManager
            return when (staggeredGridLayout.orientation) {
                StaggeredGridLayoutManager.HORIZONTAL -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
                else -> ItemTouchHelper.RIGHT
            }
        }
    };

    /**
     * Returns drag flags for the given layout manager.
     *
     * @param layout layout manager instance
     * @return drag flags
     */
    internal abstract fun getDragFlags(layout: RecyclerView.LayoutManager): Int

    /**
     * Returns swipe flags for the given layout manager.
     *
     * @param layout layout manager instance
     * @return swipe flags
     */
    internal abstract fun getSwipeFlags(layout: RecyclerView.LayoutManager): Int
}
