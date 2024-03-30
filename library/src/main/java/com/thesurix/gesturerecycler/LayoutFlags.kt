package com.thesurix.gesturerecycler

import androidx.recyclerview.widget.*

internal abstract class LayoutFlagsAbstract {
    internal abstract fun getDragFlags(layout: RecyclerView.LayoutManager?): Int
    internal abstract fun getSwipeFlags(layout: RecyclerView.LayoutManager?): Int
}

internal enum class LayoutFlags : LayoutFlagsAbstract() {
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
    GRID {
        override fun getDragFlags(layout: RecyclerView.LayoutManager?): Int {
            val gridLayout = layout as? GridLayoutManager ?: return 0
            return if (gridLayout.spanCount == 1) {
                getDragFlags(gridLayout.layoutManager as LinearLayoutManager)
            } else {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            }
        }

        override fun getSwipeFlags(layout: RecyclerView.LayoutManager?): Int {
            val gridLayout = layout as? GridLayoutManager ?: return 0
            return if (gridLayout.spanCount == 1) {
                getSwipeFlags(gridLayout.layoutManager as LinearLayoutManager)
            } else {
                when (gridLayout.orientation) {
                    GridLayoutManager.HORIZONTAL -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
                    else -> ItemTouchHelper.RIGHT
                }
            }
        }
    },
    STAGGERED {
        override fun getDragFlags(layout: RecyclerView.LayoutManager?): Int {
            val staggeredGridLayout = layout as? StaggeredGridLayoutManager ?: return 0
            return if (staggeredGridLayout.spanCount == 1) {
                getDragFlags(staggeredGridLayout.layoutManager as LinearLayoutManager)
            } else {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            }
        }

        override fun getSwipeFlags(layout: RecyclerView.LayoutManager?): Int {
            val staggeredGridLayout = layout as? StaggeredGridLayoutManager ?: return 0
            return if (staggeredGridLayout.spanCount == 1) {
                getSwipeFlags(staggeredGridLayout.layoutManager as LinearLayoutManager)
            } else {
                when (staggeredGridLayout.orientation) {
                    StaggeredGridLayoutManager.HORIZONTAL -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
                    else -> ItemTouchHelper.RIGHT
                }
            }
        }
    };
}
