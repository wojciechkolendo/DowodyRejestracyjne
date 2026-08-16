# R8 rules for the release build.
#
# Add rules here only with a reason. Every entry below was added because the minified build actually
# broke at runtime, not pre-emptively.

# ML Kit finds its components at startup by reading registrar class names out of the merged manifest
# and instantiating them reflectively. R8 sees no callers of those constructors and removes them,
# which leaves the component registry empty; BarcodeScanning.getClient() then throws an NPE the
# moment the scanner opens.
#
# Symptom without this rule:
#   W ComponentDiscovery: java.lang.NoSuchMethodException: ...CommonComponentRegistrar.<init> []
#   E AndroidRuntime: java.lang.NullPointerException
#       at com.google.mlkit.vision.barcode.BarcodeScanning.getClient(...)
-keep class * implements com.google.firebase.components.ComponentRegistrar {
    <init>();
}
