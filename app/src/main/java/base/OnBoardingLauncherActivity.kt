package base

import adapters.OnBoardingLaucherAdapter
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.odedtech.mff.client.R
import com.odedtech.mff.client.databinding.ActivityOnBoardingLauncherBinding
import login.LoginActivity
import signup.SignUpEmailAndMobileActivity

class OnBoardingLauncherActivity : AppCompatActivity() {

    lateinit var binding: ActivityOnBoardingLauncherBinding
    private var onBoardingLauncherAdapter: OnBoardingLaucherAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingLauncherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        setupOnboardingItems()
        setupOnboardingIndicators()

        setCurrentOnboardingIndicator(0)
        binding.layoutOnboardingViewpager.adapter = onBoardingLauncherAdapter

        binding.layoutOnboardingViewpager.registerOnPageChangeCallback(object :
            OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentOnboardingIndicator(position)
            }
        })

        binding.logInButton.setOnClickListener {
            if (binding.layoutOnboardingViewpager.currentItem + 1 < binding.layoutOnboardingViewpager.currentItem) {
                binding.layoutOnboardingViewpager.currentItem =
                    binding.layoutOnboardingViewpager.currentItem + 1;
            } else {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
        }

        binding.buttonOnboardingAction.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpEmailAndMobileActivity::class.java))

        }
    }

    private fun setupOnboardingItems() {
        val onboardingItemDTOS: MutableList<OnBoardingItemDTO> = ArrayList()
        val onboardingItem1 = OnBoardingItemDTO(
            "Open a savings account in less than 10 mins",
            "Your ID verification and digital application process is all in-app",
            getDrawable(R.drawable.screenshot_savings)
        )

        val onboardingItem2 = OnBoardingItemDTO(
            "Digital and Virtual Cards",
            "Credit cards gives you " +
                    "access to a line of credit issues by a bank, while debit cards deduct money directly from your bank account",
            getDrawable(R.drawable.screenshot_home)
        )

        val onboardingItem3 = OnBoardingItemDTO(
            "Local and multi currency accounts",
            "Send, spend, and " +
                    "receive money around world at the real exchange rate",
            getDrawable(R.drawable.screenshot_accounts)
        )

        onboardingItemDTOS.add(onboardingItem1)
        onboardingItemDTOS.add(onboardingItem2)
        onboardingItemDTOS.add(onboardingItem3)
        onBoardingLauncherAdapter = OnBoardingLaucherAdapter(applicationContext, onboardingItemDTOS)
    }

    private fun setupOnboardingIndicators() {
        val indicators = onBoardingLauncherAdapter?.let { arrayOfNulls<ImageView>(it.itemCount) }
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        if (indicators != null) {
            for (i in indicators.indices) {
                indicators[i] = ImageView(applicationContext)
                indicators[i]!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.onboarding_indicator_inactive
                    )
                )
                indicators[i]!!.layoutParams = layoutParams
                binding.onboardingIndicators.addView(indicators[i])
            }
        }
    }

    private fun setCurrentOnboardingIndicator(index: Int) {
        val childCount = binding.onboardingIndicators.childCount
        for (i in 0 until childCount) {
            val imageView = binding.onboardingIndicators.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.onboarding_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.onboarding_indicator_inactive
                    )
                )
            }
        }

    }
}