import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_app_invoke_sample/widgets/Button.dart';
import 'package:flutter_app_invoke_sample/widgets/InputField.dart';

class TransactionPage extends StatefulWidget {
  final String txnType;

  const TransactionPage({super.key, required this.txnType});

  @override
  State<StatefulWidget> createState() => TransactionPageState(txnType: txnType);
}

class TransactionPageState extends State<TransactionPage> {
  final String txnType;

  TransactionPageState({required this.txnType});

  bool orderIdVisibility = false;
  bool merchantIdVisibility = false;
  bool amountVisibility = false;
  bool paymentModeVisibility = false;
  bool cardReadModeVisibility = false;
  bool extendedInfoVisibility = false;
  bool subWalletInfoVisibility = false;
  bool gstInfoVisibility = false;
  bool printInfoVisibility = false;
  bool tipAmountVisibility = false;
  bool totalAmountVisibility = false;

  void checkVisibility() {
    switch (txnType) {
      case "STATUS":
        {
          merchantIdVisibility = false;
          orderIdVisibility = true;
          extendedInfoVisibility = false;
          printInfoVisibility = false;
          amountVisibility = false;
          subWalletInfoVisibility = false;
          gstInfoVisibility = false;
          paymentModeVisibility = false;
          cardReadModeVisibility = false;
          tipAmountVisibility = false;
          totalAmountVisibility = false;
          break;
        }

      case "VOID":
        {
          merchantIdVisibility = false;
          orderIdVisibility = true;
          extendedInfoVisibility = false;
          printInfoVisibility = false;
          amountVisibility = false;
          subWalletInfoVisibility = false;
          gstInfoVisibility = false;
          paymentModeVisibility = false;
          cardReadModeVisibility = false;
          tipAmountVisibility = false;
          totalAmountVisibility = false;
          break;
        }

      default:
        {
          merchantIdVisibility = false;
          orderIdVisibility = true;
          extendedInfoVisibility = false;
          amountVisibility = true;
          subWalletInfoVisibility = false;
          gstInfoVisibility = false;
          paymentModeVisibility = true;
          cardReadModeVisibility = false;
          tipAmountVisibility = false;
          totalAmountVisibility = false;
        }
    }
  }

  @override
  void initState() {
    checkVisibility();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    var merchantId = TextEditingController();
    var amount = TextEditingController();
    var orderId = TextEditingController();
    var paymentMode = TextEditingController();
    var cardReadMode = TextEditingController();
    var extendedInfo = TextEditingController();
    var subWalletInfo = TextEditingController();
    var gstInfo = TextEditingController();
    var printInfo = TextEditingController();
    var tipAmount = TextEditingController();
    var totalAmount = TextEditingController();
    TextEditingController response = TextEditingController();

    var channelNameECRCancel = const MethodChannel("bluetoothECRCancel");

    var channelName = const MethodChannel("appInvoke");

    Future<void> getSaleData() async {
      try {
        var result = await channelName.invokeMethod('sale', {
          "merchantId": merchantId.text,
          "amount": amount.text,
          "orderId": orderId.text,
          "paymentMode": paymentMode.text
        });
        response.text = result.toString();
      } on PlatformException catch (e) {
        response.text = e.message.toString();
      }
    }

    Future<void> getVoidData() async {
      try {
        var result = await channelName.invokeMethod(
            'void', {"merchantId": merchantId.text, "orderId": orderId.text});
        response.text = result.toString();
      } on PlatformException catch (e) {
        response.text = e.message.toString();
      }
    }

    Future<void> getStatusData() async {
      try {
        var result = await channelName.invokeMethod('status', {
          "merchantId": merchantId.text,
          "orderId": orderId.text,
        });
        response.text = result.toString();
      } on PlatformException catch (e) {
        response.text = e.message.toString();
      }
    }

    void getData() {
      switch (txnType) {
        case "SALE":
          getSaleData();
          break;
        case "VOID":
          getVoidData();
          break;
        case "STATUS":
          getStatusData();
          break;
      }
    }

    return Scaffold(
      appBar: AppBar(
        title: const Text("Payments Demno"),
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            Visibility(
              visible: merchantIdVisibility,
              child: InputField(
                inputType: TextInputType.text,
                fieldText: "Merchant ID",
                hintText: "",
                value: merchantId,
              ),
            ),
            Visibility(
                visible: amountVisibility,
                child: InputField(
                    inputType: TextInputType.numberWithOptions(decimal: true),
                    value: amount,
                    fieldText: "Amount (In Paise)",
                    hintText: "")),
            Visibility(
                visible: orderIdVisibility,
                child: InputField(
                    inputType: TextInputType.text,
                    value: orderId,
                    fieldText: "Order ID",
                    hintText: "")),
            Visibility(
                visible: paymentModeVisibility,
                child: InputField(
                    inputType: TextInputType.text,
                    value: paymentMode,
                    fieldText: "Payment Mode",
                    hintText: "eg:- ALL/CARD/QR")),
            Visibility(
              visible: cardReadModeVisibility,
              child: InputField(
                  inputType: TextInputType.text,
                  value: cardReadMode,
                  fieldText: "Card Read Mode",
                  hintText: "eg:- ALL/INSERT/TAP/SWIPE"),
            ),
            Visibility(
              visible: extendedInfoVisibility,
              child: InputField(
                  inputType: TextInputType.text,
                  value: extendedInfo,
                  fieldText: "Extended Info",
                  hintText:
                      "eg :- extendInfo://values?productValidationValue=12764874894894898&merchantTxnId=82938938983&caNumber=34567777"),
            ),
            Visibility(
              visible: subWalletInfoVisibility,
              child: InputField(
                  inputType: TextInputType.text,
                  value: subWalletInfo,
                  fieldText: "Sub Wallet Info",
                  hintText: "eg :- FOOD:2000|GIFT:1000"),
            ),
            Visibility(
              visible: gstInfoVisibility,
              child: InputField(
                  inputType: TextInputType.text,
                  value: gstInfo,
                  fieldText: "GST Info",
                  hintText:
                      "eg :- gstInformation://values?gstIn=08TESTF0078P1ZP&gstBrkUp=CGST:10|SGST:10|IGST:10|CESS:10|GSTIncentive:10|GSTPCT:10&invoiceNo=Invoice34234321&invoiceDate=20210512142919"),
            ),
            Visibility(
              visible: printInfoVisibility,
              child: InputField(
                  inputType: TextInputType.text,
                  value: printInfo,
                  fieldText: "Print Info",
                  hintText:
                      "eg :- printInfo://values?merchantTxnId=82938938983&caNumber=34567777&billNumber=xyz123"),
            ),
            Visibility(
                visible: tipAmountVisibility,
                child: InputField(
                    inputType: TextInputType.numberWithOptions(decimal: true),
                    value: tipAmount,
                    fieldText: "Tip Amount (In Paise)",
                    hintText: "")),
            Visibility(
                visible: totalAmountVisibility,
                child: InputField(
                    inputType: TextInputType.numberWithOptions(decimal: true),
                    value: totalAmount,
                    fieldText: "Total Amount (In Paise)",
                    hintText: "")),
            Center(
              child: Button(
                  btnText: txnType,
                  onPress: () {
                    getData();
                  }),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
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
          ],
        ),
      ),
    );
  }
}
