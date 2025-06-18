using System;
using Microsoft.Maui.Controls;
using MauiApp1;

#if ANDROID
using Android.App;
using Microsoft.Maui.Platform;
#endif

namespace MauiApp1
{
    public partial class MainPage : ContentPage
    {
        int count = 0;

        public MainPage()
        {
            InitializeComponent();

            MessagingCenter.Subscribe<object, string>(this, "PaymentResult", (sender, result) =>
            {
                // Update the label on the main thread
                MainThread.BeginInvokeOnMainThread(() =>
                {
                    PaymentResultLabel.Text = result;
                });
            });
        }

        private void OnPrintClicked(object sender, EventArgs e)
        {
#if ANDROID
            // Get the current activity
            var activity = Platform.CurrentActivity;
            if (activity is MainActivity mainActivity)
            {
                // Call the method in MainActivity
                mainActivity.PrintDemoPage();
            }
#endif
        }

        private void OnSubmitClicked(object sender, EventArgs e)
        {
            string amount = AmountEntry.Text;
            string orderId = OrderIdEntry.Text;
            string payMode = PayModeEntry.Text;

#if ANDROID
            // Get the current activity
            var activity = Platform.CurrentActivity;
            if (activity is MainActivity mainActivity)
            {
                // Call the method in MainActivity
                mainActivity.CollectPayment(amount, orderId, payMode);
            }
#endif
        }

        protected override void OnDisappearing()
        {
            base.OnDisappearing();
            MessagingCenter.Unsubscribe<object, string>(this, "PaymentResult");
        }
    }

        //private void OnCounterClicked(object sender, EventArgs e)
        //{
        //    count++;

        //    if (count == 1)
        //        CounterBtn.Text = $"Clicked {count} time";
        //    else
        //        CounterBtn.Text = $"Clicked {count} times";

        //    SemanticScreenReader.Announce(CounterBtn.Text);
        //}


    
}
