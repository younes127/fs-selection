## fs-selection
Simple library to use [File Selector](https://play.google.com/store/apps/details?id=com.ioapps.fileselector) as File Picker.
If the user does not have 'File Selector', it displays a dialog to request the installation.

[![](https://jitpack.io/v/ioapps-dev/fs-selection.svg)](https://jitpack.io/#ioapps-dev/fs-selection)

## Dependency

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
	repositories {
        maven { url "https://jitpack.io" }
    }
}
```

Then, add the library to your module `build.gradle`
```gradle
dependencies {
    implementation 'com.github.ioapps-dev:fs-selection:latest.release.here'
}
```

## Features
- Choose unique file
- Choose unique file by extension
- Choose multiple files
- Choose multiple files by extension
- Choose unique folder
- Choose multiple folder
- Choose any (file or folder) unique
- Choose any (file or folder) multiple

<table><tr><td>
<img width="200" alt="portfolio_view" src="https://raw.github.com/ioapps-dev/fs-selection/master/screenshots/screenshot-1.png">
</td><td>
<img width="200" alt="portfolio_view" src="https://raw.github.com/ioapps-dev/fs-selection/master/screenshots/screenshot-2.png">
</td><td>
<img width="200" alt="portfolio_view" src="https://raw.github.com/ioapps-dev/fs-selection/master/screenshots/screenshot-3.png">
</td></tr></table>

## Usage
There is a [sample](https://github.com/ioapps-dev/fs-selection/tree/master/sample) provided which shows how to use the library

```java
    private FileSelector fileSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileSelector = new FileSelector(this);

        // Display file picker
        fileSelector.chooseUniqueFileByExtension("Choose File", "Select an image (jpg)", "jpg");
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == fileSelector.getRequestCode()) {
            if(resultCode == RESULT_OK) {
                // Get selected files
                Uri[] uris = fileSelector.loadResult(intent);
            } else {
                // No selection
            }
        }
    }
    
```

License
--------

    Copyright 2017 IO Apps

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
