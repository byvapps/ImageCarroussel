package com.inlacou.imagecarroussel

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout

class ImageCarousel @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
	: FrameLayout(context, attrs, defStyleAttr) {

	private var viewPager: ViewPager? = null
	private var pagerAdapter: PicturesFragmentPagerAdapter? = null

	var model: ImageCarouselMdl = ImageCarouselMdl(null, listOf())
		set(value) {
			field = value
			controller.model = value
			populate()
		}
	private lateinit var controller: ImageCarouselCtrl

	init {
		this.initialize()
		setListeners()
		populate()
	}

	protected fun initialize() {
		val rootView = View.inflate(context, R.layout.view_imagecarousel, this)
		viewPager = findViewById(R.id.pager)
		initialize(rootView)
	}

	fun initialize(view: View) {
		controller = ImageCarouselCtrl(view = this, model = model)
	}

	fun populate() {
		/** Instantiating FragmentPagerAdapter  */
		model.fragmentManager?.let { fragmentManager ->
			pagerAdapter = PicturesFragmentPagerAdapter(
					fragmentManager = fragmentManager,
					urls = model.urls.toArrayList(),
					positionDisplay = model.positionDisplay,
					showTopShadow = model.showTopShadow) {
				controller.onClick(it)
			}

			/** Setting the pagerAdapter to the pager object  */
			viewPager?.adapter = pagerAdapter
		}
	}

	private fun setListeners() {
		viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
			override fun onPageScrollStateChanged(state: Int) {}

			override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

			override fun onPageSelected(position: Int) {
				model.onPageShown?.invoke(position)
			}
		})
	}

	fun nextPage(){
		Log.d("nextPage", "currentItem: ${viewPager?.currentItem}")
		if(model.autoSwipe.continuous && viewPager?.currentItem==model.urls.size-1){
			viewPager?.currentItem = 0
		}else {
			viewPager?.arrowScroll(View.FOCUS_RIGHT)
		}
	}

	private fun <T> List<T>.toArrayList(): ArrayList<T>{
		val result = ArrayList<T>()
		this.forEach { result.add(it) }
		return result
	}

}