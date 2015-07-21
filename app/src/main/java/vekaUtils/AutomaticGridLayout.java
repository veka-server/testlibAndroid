package vekaUtils;

import java.util.Vector;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;

/**
 * GridLayout with automatic column number. <br/>
 * the width of the column is the same as the bigger width child<br/>
 * <br/>
 * default width = 10<br/>
 * default space between Column Horizontaly = 5 <br/>
 * 
 * <h3>XML Param</h3>
 *  xmlns:app="http://schemas.android.com/apk/res-auto" <br/>
 *  app:spaceBetweenColumnHorizontaly="auto"  <br/>
 *  app:maxColumnNumber="2" <br/>
 *
 *  <br/>
 * @author veka
 *
 */
public class AutomaticGridLayout extends GridLayout{

	protected Vector<View> child = new Vector<View>();
	public int columnWidth = 10;
	public int RealMaxColumnWidth = 0;

	private int spaceBetweenColumnHorizontaly = 5;
	private boolean spaceBetweenColumnAuto = false;
	private int maxColumnNumber = 0;

	public AutomaticGridLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public AutomaticGridLayout(Context context) {
		super(context);
	}
	
	/**
	 * parse custom attribute from XML and init them
	 * @param attrs
	 */
	private void init(AttributeSet attrs) { 
		
	    TypedArray a = getContext().obtainStyledAttributes( attrs, com.vekandroid.vekautils.R.styleable.AutomaticGridLayout);
	    int tmpSpaceBetweenColumnHorizontaly = a.getInt(com.vekandroid.vekautils.R.styleable.AutomaticGridLayout_spaceBetweenColumnHorizontaly, spaceBetweenColumnHorizontaly);
	    maxColumnNumber = a.getInt(com.vekandroid.vekautils.R.styleable.AutomaticGridLayout_maxColumnNumber,Integer.MAX_VALUE);
	    
	    if(tmpSpaceBetweenColumnHorizontaly < 0)
	    	spaceBetweenColumnAuto = true;
	    else
	    	spaceBetweenColumnHorizontaly = tmpSpaceBetweenColumnHorizontaly;

	    a.recycle();
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		for (int i = 0; i < this.getChildCount(); i++){
			child.addElement(this.getChildAt(i));
		}
		
		resetColumnCount();
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		
		getMaxChildRealWidth();

		// if child maxwidth != maxRealWidth
		if(RealMaxColumnWidth != columnWidth)
			refresh();

	}
	
	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		refresh();
	}

	@Override
	public void addView(View child) {
		this.child.addElement(child);
		super.addView(child);
		refresh(); // refresh all childs because margin can change with a new child
	}
			
	@Override
	public void removeAllViews() {
		super.removeAllViews();
		child.removeAllElements();
	}
	
	@Override
	public void removeView(View view) {
		super.removeView(view);
		child.remove(view);
	}

	@Override
	public void removeViewAt(int index) {
		super.removeViewAt(index);
		child.remove(index);
	}

	/**
	 * Refresh childs view and column number
	 */
	public void refresh() {

		super.removeAllViews();

		resetColumnCount();
		
		for(int i=0; i< child.size(); ++i){
			resetLayout(child.get(i));
			super.addView(child.get(i));
		}
	}

	/**
	 * reset position (rowSpec and columnSpec) of a view
	 * @param view
	 */
	private void resetLayout(View view) {
		GridLayout.LayoutParams resetLayout = new GridLayout.LayoutParams();
		GridLayout.LayoutParams childLayout = ((GridLayout.LayoutParams) view.getLayoutParams());
		childLayout.rowSpec = resetLayout.rowSpec;
		childLayout.columnSpec = resetLayout.columnSpec;
		childLayout.leftMargin = spaceBetweenColumnHorizontaly;
		childLayout.rightMargin = spaceBetweenColumnHorizontaly;
		
		// force width of childs if default childs width  is greater than screen width 
		int widthScreen = getContext().getApplicationContext().getResources().getDisplayMetrics().widthPixels;
		if(columnWidth > widthScreen)
			childLayout.width = columnWidth;
		
		view.setLayoutParams(childLayout);
		setUseDefaultMargins(true);
	}

	/**
	 * Calcul and define number of columns
	 */
	protected void resetColumnCount() {

		getMaxChildWidthFromLayout();

		// force width if wrap_content
		if(RealMaxColumnWidth > columnWidth){
			columnWidth = RealMaxColumnWidth;
			RealMaxColumnWidth = 0;
		}
		
		setColumnCount(getCountColumnPossible());
	}

	/**
	 * calcul number of column
	 * @return number of column
	 */
	private int getCountColumnPossible() {
		int widthScreen = getContext().getApplicationContext().getResources().getDisplayMetrics().widthPixels;
		
		int columnCount = (int) Math.floor(widthScreen/columnWidth);
		
		// fix space between column
		if(!spaceBetweenColumnAuto)
			columnCount = (int) Math.floor(widthScreen/(columnWidth+(2*spaceBetweenColumnHorizontaly)));

		// minimum one column
		columnCount = (columnCount <= 0 )? 1 : columnCount;

		// max column count
		columnCount = (columnCount > maxColumnNumber )? maxColumnNumber : columnCount;		
		
		// automatic space between column for max childs per column and center them
		if(spaceBetweenColumnAuto)
			spaceBetweenColumnHorizontaly = (widthScreen - (columnCount*columnWidth))/(columnCount*2);
					
		return columnCount;
	}

	/**
	 * get Max child width from layoutParam
	 */
	public void getMaxChildWidthFromLayout(){
		for(int i=0; i< child.size(); ++i) {
			int w = child.get(i).getLayoutParams().width;
			if(columnWidth < w)
				columnWidth = w;
		}
		
		// debut d'une securité pour enfant trops grand
		// bug surement encore mais ne crash plus
		// surement a cause des padding/margin des shades, a verifier 
		int widthScreen = getContext().getApplicationContext().getResources().getDisplayMetrics().widthPixels;
		if(columnWidth > widthScreen){
			columnWidth = widthScreen-(2*spaceBetweenColumnHorizontaly);
		}
	}
	
	/**
	 * get Max real child width 
	 */
	public void getMaxChildRealWidth(){
		for (int i = 0; i < this.getChildCount(); i++){
			int w = child.get(i).getWidth();
			if(RealMaxColumnWidth < w)
				RealMaxColumnWidth = w;
		}	


	}
	
	

}
