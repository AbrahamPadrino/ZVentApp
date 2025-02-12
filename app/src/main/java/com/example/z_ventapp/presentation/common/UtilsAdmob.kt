package com.example.z_ventapp.presentation.common

import android.content.Context
import com.example.z_ventapp.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UtilsAdmob @Inject constructor(@ApplicationContext private val context: Context) {

    var interstitial: InterstitialAd? = null

    fun initInterstitial() {
        InterstitialAd.load(
            context,
            context.getString(R.string.id_admob_intersticial),
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitial = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitial = null
                }
            }
        )
    }

}