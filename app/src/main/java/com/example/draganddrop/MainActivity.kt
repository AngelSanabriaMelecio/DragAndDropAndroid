package com.example.draganddrop

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imgTarget1: ImageView = findViewById(R.id.imgTarget1)
        val imgTarget2: ImageView = findViewById(R.id.imgTarget2)
        val imgTarget3: ImageView = findViewById(R.id.imgTarget3)

        val imgDrag1: ImageView = findViewById(R.id.imgDrag1)
        val imgDrag2: ImageView = findViewById(R.id.imgDrag2)
        val imgDrag3: ImageView = findViewById(R.id.imgDrag3)

        imgTarget1.tag = "figure1"
        imgTarget2.tag = "figure2"
        imgTarget3.tag = "figure3"

        imgDrag1.tag = "figure1"
        imgDrag2.tag = "figure2"
        imgDrag3.tag = "figure3"

        imgDrag1.setOnLongClickListener(longClickListener)
        imgDrag2.setOnLongClickListener(longClickListener)
        imgDrag3.setOnLongClickListener(longClickListener)

        imgTarget1.setOnDragListener(dragListener)
        imgTarget2.setOnDragListener(dragListener)
        imgTarget3.setOnDragListener(dragListener)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private val longClickListener = View.OnLongClickListener { v ->
        val item = ClipData.Item(v.tag as? CharSequence)

        val dragData = ClipData(
            v.tag as CharSequence,
            arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
            item
        )

        val myShadow = MyDragShadowBuilder(v)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            v.startDragAndDrop(dragData, myShadow,null,0)
        } else {
            v.startDrag(dragData, myShadow,null,0)
        }

        true
    }

    private class MyDragShadowBuilder(val v: View):View.DragShadowBuilder(v){
        override fun onProvideShadowMetrics(size: Point?, touch: Point?) {
            size?.set(view.width,view.height)
            touch?.set(view.width/2,view.height/2)
        }

        override fun onDrawShadow(canvas: Canvas?) {
            v.draw(canvas)
        }
    }
    private val dragListener = View.OnDragListener { v, event ->
        val receiverView:ImageView = v as ImageView

        var lblStatus: TextView = findViewById(R.id.lblStatus)

        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                lblStatus.text = "Estas arrastrando una figura"
                true
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                if(event.clipDescription.label == receiverView.tag as String) {
                    receiverView.setColorFilter(Color.GREEN)
                    lblStatus.text = "Imagen Correcta!"
                } else {
                    receiverView.setColorFilter(Color.RED)
                    lblStatus.text = "No Imagen Inorrecta!"
                }
                v.invalidate()
                true
            }

            DragEvent.ACTION_DRAG_LOCATION ->
                true

            DragEvent.ACTION_DRAG_EXITED -> {
                if(event.clipDescription.label == receiverView.tag as String) {
                    receiverView.setColorFilter(Color.YELLOW)
                    lblStatus.text = "Casi la tenias!"
                    v.invalidate()
                }
                true
            }

            DragEvent.ACTION_DROP -> {
                lblStatus.text = "Soltaste la imagen!"
                true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                true
            }

            else -> {
                false
            }
        }
    }
}