package vekaUtils;

import android.content.Context;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public abstract class AdMobs {
	
	
//.addTestDevice("AF11C0F4D5095E1F6CB5B5C6AAF33988")

	
	private InterstitialAd mInterstitialAd;
	private String ad_unit_id;
	private Context ctx;
	private String[] testDevice;
	   
	   
	   public AdMobs(Context ctx, String ad_unit_id, String[] testDevice){
		   this.ad_unit_id = ad_unit_id;
		   this.ctx = ctx;
		   this.testDevice = testDevice;
		   
		   initadMobs();		   
	   }

	   public AdMobs(Context ctx, String ad_unit_id){
		   this(ctx, ad_unit_id, new String[0]);
	   }

	   private void initadMobs() {

		   // instantier la pub
	       mInterstitialAd = new InterstitialAd(ctx);
	       mInterstitialAd.setAdUnitId(ad_unit_id);
	       requestNewInterstitial();

	       mInterstitialAd.setAdListener(new AdListener() {
	    	   
	    	   @Override
	           public void onAdClosed() {
	    		   AdMobs.this.onAdClosed();
	    		   requestNewInterstitial();
	    	   }
	           
	           @Override
		        public void onAdLoaded() {        	   
		        	super.onAdLoaded();
	    		   AdMobs.this.onAdLoaded();		            
	           }
	           
				@Override
				public void onAdFailedToLoad(int errorCode) {
					super.onAdFailedToLoad(errorCode);
					AdMobs.this.onAdFailedToLoad(errorCode);
				}
				
				@Override
				public void onAdLeftApplication() {
					super.onAdLeftApplication();
				   AdMobs.this.onAdLeftApplication();
				   requestNewInterstitial();
				  }
				 	           
				@Override
				public void onAdOpened() {
					super.onAdOpened();
				   AdMobs.this.onAdOpened();
				   requestNewInterstitial();
				}
   
	           
	       });
		
	   }
	   
	   private void requestNewInterstitial() {

		   com.google.android.gms.ads.AdRequest.Builder preBuild = new AdRequest.Builder();

		   // ajoute les id des device de test
		   for (int i = 0; i < this.testDevice.length; i++)
			   preBuild.addTestDevice(testDevice[i]);
		   
		   AdRequest adRequest = preBuild.build();
		   
	       mInterstitialAd.loadAd(adRequest);
	   }
	   
	   public void show(){
		   if(mInterstitialAd.isLoaded())
			   mInterstitialAd.show();
	   }

	
	   public abstract void onAdClosed();
	   public abstract void onAdFailedToLoad(int errorCode);
	   public abstract void onAdLoaded();
	   public abstract void onAdOpened();
	   public abstract void onAdLeftApplication();	

}
