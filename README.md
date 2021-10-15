![math-view](https://user-images.githubusercontent.com/32610660/125827642-bbb31432-eea1-443b-87e8-47bbb2254172.png)
# math-view
A Library for displaying math equation in Android

[![](https://jitpack.io/v/derysudrajat/math-view.svg)](https://jitpack.io/#derysudrajat/math-view)

## Setup

Add this to `build.gradle` (project)

```gradle
allprojects {
  repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
}
```

Add this dependency to `build.grdle` (module) or if you using gradle 7+ put it to `setting.gradle`
```gradle
dependencies {
  implementation 'com.github.derysudrajat:math-view:1.0.1'
}
```

## How to use

Create MathView in your XML layout

```xml
<io.github.derysudrajat.mathview.MathView
  android:id="@+id/mathView"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"/>
```

You can put [LaTeX](https://www.overleaf.com/learn/latex/Mathematical_expressions) expression to set into the formula

```kotlin
val formula = "2a+4b\\sqrt{\\frac{4x-2^{6}}{ax^2+57}}+\\frac{3}{2}"
mathView.formula = formula
```

## Screenshot

  
<a><img src="https://user-images.githubusercontent.com/32610660/137457393-2516e36b-6fc7-4a0d-9a88-e3e0b50af8f7.jpg" width=50% alt="Sample App"></a>
