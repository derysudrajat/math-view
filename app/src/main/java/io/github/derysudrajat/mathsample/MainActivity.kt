package io.github.derysudrajat.mathsample

import android.annotation.SuppressLint
import android.graphics.Color.rgb
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.derysudrajat.mathsample.databinding.ActivityMainBinding
import io.github.derysudrajat.mathview.MathView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            val baseText = "2a+4b\\sqrt{\\frac{4x-2^{6}}{ax^2+57}}+\\frac{3}{2}"
            mathView.formula = baseText
            mathView.setTextColor(R.color.primary)
//            mathView.setTextColor("#76081C")
//            mathView.setTextColor(MathView.RGB(118, 8, 28))
//            mathView.setTextColor(MathView.RGB.WHITE)
            edtInputMath.setText(baseText)
            btnRender.setOnClickListener {
                val text = edtInputMath.text.toString()
                mathView.loadFromMathJax = binding.switchButton.isChecked
                if (text.isNotBlank()) mathView.formula = text
            }
        }
    }
}
