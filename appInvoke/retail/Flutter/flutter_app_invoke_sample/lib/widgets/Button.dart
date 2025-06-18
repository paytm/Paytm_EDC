import 'package:flutter/material.dart';

class Button extends StatelessWidget {
  final String btnText;
  final VoidCallback onPress;

  const Button({super.key, required this.btnText, required this.onPress});

  @override
  Widget build(BuildContext context) {
    return ElevatedButton(
      onPressed: () {
        onPress();
      },
      style: ElevatedButton.styleFrom(
          backgroundColor: Colors.blue,
          shadowColor: Colors.white,
          shape: const RoundedRectangleBorder(
              borderRadius: BorderRadius.all(Radius.circular(10)))),
      child: Text(btnText),
    );
  }
}
