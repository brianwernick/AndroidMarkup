Android Markup
============

An Android Widget and basic parser for supporting WYSIWYG editing of text in
to markup languages such as HTML or Markdown

Proof of Concept
------
This project has been deemed a proof of concept and won't be maintained



Use
-------
The latest AAR (Android Archive) files can be downloaded from JCenter [AndroidMarkup][1]

Or included in your gradle dependencies

```gradle
repositories {
    //Because this is an early release, it hasn't been uploaded to jCenter
    maven { url 'https://dl.bintray.com/brianwernick/maven' }
}

dependencies {
    //...
    compile 'com.devbrackets.android:androidmarkup:0.2.0'
}
```

License
-------

    Copyright 2016 Brian Wernick

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


Attribution
-----------
* Uses [AppCompat-v7](http://developer.android.com/tools/support-library/features.html#v7-appcompat) licensed under [Apache 2.0][Apache 2.0]
* Uses [CommonMark-java](https://github.com/atlassian/commonmark-java) licensed under [2-Clause BSD](https://opensource.org/licenses/BSD-2-Clause)
* Uses [Kotlin](https://github.com/JetBrains/kotlin) licensed under [Apache 2.0][Apache 2.0]


 [1]: https://bintray.com/brianwernick/maven/AndroidMarkup/view#files
 [2]: http://devbrackets.com/dev/libs/androidmarkup.html
 [3]: http://devbrackets.com/dev/libs/docs/androidmarkup/0.2.0/index.html
 [Apache 2.0]: https://opensource.org/licenses/Apache-2.0
