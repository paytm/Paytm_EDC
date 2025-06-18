import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_app_invoke_sample/pages/TransactionPage.dart';

import '../widgets/Button.dart';

class PaymentsHomePage extends StatelessWidget {
  const PaymentsHomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text("Payments Home Page"),
        ),
        body: Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            Column(mainAxisAlignment: MainAxisAlignment.spaceAround, children: [
              Button(
                  btnText: "SALE",
                  onPress: () {
                    Navigator.push(context,
                        MaterialPageRoute(builder: (context) {
                      return const TransactionPage(txnType: "SALE");
                    }));
                  }),
              Button(
                  btnText: "STATUS",
                  onPress: () {
                    Navigator.push(context,
                        MaterialPageRoute(builder: (context) {
                      return const TransactionPage(txnType: "STATUS");
                    }));
                  }),
              Button(
                  btnText: "VOID",
                  onPress: () {
                    Navigator.push(context,
                        MaterialPageRoute(builder: (context) {
                      return const TransactionPage(txnType: "VOID");
                    }));
                  })
            ])
          ],
        ));
  }
}
