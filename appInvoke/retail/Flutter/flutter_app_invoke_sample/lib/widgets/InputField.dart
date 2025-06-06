import 'package:flutter/material.dart';

class InputField extends StatelessWidget {
  final String fieldText;
  final String hintText;
  final TextEditingController value;
  final TextInputType inputType;

  InputField(
      {super.key,
      required this.fieldText,
      required this.hintText,
      required this.value,
      required this.inputType});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Text(fieldText),
          ),
          TextField(
            controller: value,
            keyboardType: inputType,
            decoration: InputDecoration(
                hintText: hintText,
                border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                    borderSide: const BorderSide(color: Colors.green))),
          )
        ],
      ),
    );
  }
}
