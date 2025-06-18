import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_app_invoke_sample/pages/TransactionPage.dart';

import '../widgets/Button.dart';

class PrinterHomePage extends StatelessWidget {
  PrinterHomePage({super.key});

  static const channelPrinter = MethodChannel("printer");

  final TextEditingController response = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Printer Activity"),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Expanded(
              child: Image.asset('assets/images/ic_all_in_one_pos.png',
                  fit: BoxFit.fitWidth),
            ),
            Expanded(
              child: Center(
                  child: TextField(
                      maxLines: null,
                      controller: response,
                      keyboardType: TextInputType.text,
                      decoration: InputDecoration(
                          hintText: "Response",
                          border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(12),
                              borderSide:
                                  const BorderSide(color: Colors.green))))),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: <Widget>[
                Expanded(
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: ElevatedButton(
                      onPressed: () {
                        printBitmap();
                      },
                      child: Text('Print Bitmap'),
                    ),
                  ),
                ),
                Expanded(
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: ElevatedButton(
                      onPressed: () {
                        printData();
                      },
                      child: Text('Print Data'),
                    ),
                  ),
                ),
              ],
            )
          ],
        ),
      ),
    );
  }

  Future<void> printBitmap() async {
    try {
      final Uint8List imageData =
          await loadImageFromAssets('assets/images/receipt.png');
      var result = await channelPrinter.invokeMethod('printBitmap', imageData);
      response.text = result.toString();
    } on PlatformException catch (e) {
      response.text = e.message.toString();
    }
  }

  Future<Uint8List> loadImageFromAssets(String assetPath) async {
    final ByteData data = await rootBundle.load(assetPath);
    return data.buffer.asUint8List();
  }

  Future<void> printData() async {
    try {
      final Uint8List imageData =
          await loadImageFromAssets('assets/images/ic_all_in_one_pos.png');
      var result = await channelPrinter.invokeMethod('printData',
          {"amount": "1000", "orderId": "order123", "image": imageData});
      response.text = result.toString();
    } on PlatformException catch (e) {
      response.text = e.message.toString();
    }
  }
}
