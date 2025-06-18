import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_app_invoke_sample/pages/PaymentsHomePage.dart';
import 'package:flutter_app_invoke_sample/pages/PrinterHomePage.dart';
import 'package:flutter_app_invoke_sample/pages/TransactionPage.dart';

import '../widgets/Button.dart';

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  static const channelNameAppInvoke = MethodChannel("appInvoke");
  static const channelNamePrinter = MethodChannel("printer");

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text("Home Page"),
        ),
        body: Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            Column(mainAxisAlignment: MainAxisAlignment.spaceAround, children: [
              Button(
                  btnText: "Payments SDK Use",
                  onPress: () {
                    Navigator.push(context,
                        MaterialPageRoute(builder: (context) {
                      return const PaymentsHomePage();
                    }));
                  }),
              Button(
                  btnText: "Printer SDK Use",
                  onPress: () {
                    Navigator.push(context,
                        MaterialPageRoute(builder: (context) {
                      return PrinterHomePage();
                    }));
                  })
            ])
          ],
        ));
  }
}
