# json2dart
## A plugin that can convert json into a dart file that conforms to the json_serializable rule

If you don't have json_serializable dependency，you nee dependency json_serializable

  Instructions see [json_serializable](https://pub.dev/packages/json_serializable)

1. Depend on it

   Add this to your package's pubspec.yaml file:

   ```
   dependencies:
     json_serializable: ^3.3.0
   ```

   

2. Install it 

   pou can install packages from the command line:

   ```
   pub get
   ```

And then 

![](https://tva1.sinaimg.cn/large/007S8ZIlly1gebx59i3vrg31fz0u0e82.gif)

Run the shell after generate the dart file

``` flutter packages pub run build_runner build  ```



