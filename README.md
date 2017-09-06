[ ![Download](https://api.bintray.com/packages/pulimet/utils/indicators/images/download.svg) ](https://bintray.com/pulimet/utils/indicators/_latestVersion)      [![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

<img align="right" src="https://raw.githubusercontent.com/Pulimet/Indicators-Library/master/art/demo2.gif">

# Indicators-Library

Indicators is custom view that indicates selected page.

# Installation

- Add the dependency from jCenter to your app's (not project) build.gradle file:

```sh
repositories {
    jcenter()
}

dependencies {
    compile 'net.alexandroid.utils:indicators:1.3'
}
```



# Release notes
* 1.3 - Wrap content and padding support
* 1.2 - Smooth indicator change and clickabillity


# How to use it

Add view:
```xml
<net.alexandroid.utils.indicators.IndicatorsView
    android:id="@+id/indicatorsView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"

    app:indicatorSize="20dp"
    app:paddingBetweenIndicators="16dp"

    app:selectedDrawable="@drawable/custom_selected"
    app:unSelectedDrawable="@drawable/custom_unselected"
    
    (options usable when viewpager is not attached)
    app:numberOfIndicators="10"
    app:selectedIndicator="5"/>
```

And in your code:
```java
private IndicatorsView mIndicatorsView;
...
 
mIndicatorsView = findViewById(R.id.indicatorsView);

mIndicatorsView.setViewPager(viewPager);

mIndicatorsView.setSmoothTransition(true);

mIndicatorsView.setIndicatorsClickChangePage(true);

mIndicatorsView.setIndicatorsClickListener(new IndicatorsView.OnIndicatorClickListener() {
    @Override
    public void onClick(int indicatorNumber) {
        MyLog.d("Click on: "+ indicatorNumber);
    }
});

// usable when viewpager is not attached
mIndicatorsView.setSelectedIndicator(2);
```

 <br>  <br>  <br> 
# License

```
Copyright 2016 Alexey Korolev

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
