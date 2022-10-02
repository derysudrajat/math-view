package io.github.derysudrajat.mathsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.derysudrajat.mathsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            val baseText = "2a+4b\\sqrt{\\frac{4x-2^{6}}{ax^2+57}}+\\frac{3}{2}"
            mathView.formula = baseText

            edtInputMath.setText(baseText)
            btnRender.setOnClickListener {
                val text = edtInputMath.text.toString()
                mathView.loadFromMathJax = binding.switchButton.isChecked
                if (text.isNotBlank()) mathView.formula = text
            }
        }
    }
}
