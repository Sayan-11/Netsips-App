package `in`.netsips.netsipsapp.helper

import `in`.netsips.netsipsapp.R
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToDeleteShareCallback(context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {


    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.delete_size)
    private val shareIcon = ContextCompat.getDrawable(context, R.drawable.share_size)
    private val intrinsicWidth = deleteIcon!!.intrinsicWidth
    private val intrinsicHeight = deleteIcon!!.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#f44336")
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        /**
         * To disable "swipe" for specific item return 0 here.
         * For example:
         * if (viewHolder?.itemViewType == YourAdapter.SOME_TYPE) return 0
         * if (viewHolder?.adapterPosition == 0) return 0
         */
        if (viewHolder.adapterPosition == 10) return 0
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
        // FOR DRAG AND DROP
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(
                c,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }
    if(dX>0){
        //swipe Right
        background.color = Color.parseColor("#616161")
        background.setBounds(
            itemView.left,
            itemView.top,
            itemView.left + dX.toInt(),
            itemView.bottom
        )
        background.draw(c)

        // Calculate position of delete icon

        val shareIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val shareIconMargin = 40
        val shareIconLeft = itemView.left + shareIconMargin
        val shareIconRight = itemView.left + shareIconMargin+ intrinsicWidth
        val shareIconBottom = shareIconTop + intrinsicHeight
        shareIcon!!.setBounds(shareIconLeft, shareIconTop, shareIconRight, shareIconBottom)
        shareIcon.draw(c)

    }
    else if(dX<0){
        background.color = backgroundColor
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(c)

        // Calculate position of delete icon
        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin =40
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        // Draw the delete icon
        deleteIcon!!.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon.draw(c)

    }else{
        background.bounds= Rect(0,0,0,0)
    }

        super.onChildDraw(c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive)


    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

//    abstract class SwipeToDeleteCallback2(context: Context) :
//        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//
//        private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.outline_delete_24)
//        private val intrinsicWidth = deleteIcon!!.intrinsicWidth
//        private val intrinsicHeight = deleteIcon!!.intrinsicHeight
//        private val background = ColorDrawable()
//        private val backgroundColor = Color.parseColor("#616161")
//        private val clearPaint =
//            Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
//
//
//        override fun getMovementFlags(
//            recyclerView: RecyclerView,
//            viewHolder: RecyclerView.ViewHolder
//        ): Int {
//            /**
//             * To disable "swipe" for specific item return 0 here.
//             * For example:
//             * if (viewHolder?.itemViewType == YourAdapter.SOME_TYPE) return 0
//             * if (viewHolder?.adapterPosition == 0) return 0
//             */
//            if (viewHolder.adapterPosition == 10) return 0
//            return super.getMovementFlags(recyclerView, viewHolder)
//        }
//
//        override fun onMove(
//            recyclerView: RecyclerView,
//            viewHolder: RecyclerView.ViewHolder,
//            target: RecyclerView.ViewHolder
//        ): Boolean {
//            return false
//        }
//
//        override fun onChildDraw(
//            c: Canvas,
//            recyclerView: RecyclerView,
//            viewHolder: RecyclerView.ViewHolder,
//            dX: Float,
//            dY: Float,
//            actionState: Int,
//            isCurrentlyActive: Boolean
//        ) {
//
//            val itemView = viewHolder.itemView
//            val itemHeight = itemView.bottom - itemView.top
//            val isCanceled = dX == 0f && !isCurrentlyActive
//
//            if (isCanceled) {
//                clearCanvas(
//                    c,
//                    itemView.right + dX,
//                    itemView.top.toFloat(),
//                    itemView.right.toFloat(),
//                    itemView.bottom.toFloat()
//                )
//                super.onChildDraw(
//                    c,
//                    recyclerView,
//                    viewHolder,
//                    dX/4,
//                    dY,
//                    actionState,
//                    isCurrentlyActive
//                )
//                return
//            }
//
//            // Draw the red delete background
//            background.color = backgroundColor
//            background.setBounds(
//                itemView.left,
//                itemView.top,
//                itemView.left + dX.toInt(),
//                itemView.bottom
//            )
//            background.draw(c)
//
//
//            // Calculate position of delete icon
//            val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
//            val deleteIconMargin = (itemHeight - intrinsicHeight)
//            val deleteIconLeft = itemView.left + deleteIconMargin
//            val deleteIconRight = itemView.left + deleteIconMargin + intrinsicWidth
//            val deleteIconBottom = deleteIconTop + intrinsicHeight
//
//            // Draw the delete icon
//            deleteIcon!!.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
//            deleteIcon.draw(c)
//
//            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//        }
//
//        private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
//            c?.drawRect(left, top, right, bottom, clearPaint)
//        }
//    }
}