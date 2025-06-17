using Android.App;
using Android.Content.PM;
using Android.OS;
using Com.Paytm.Printgenerator.Printer;
using Com.Paytm.Printgenerator.Page;
using Android.Graphics;
using Com.Paytm.Printgenerator;
using Page = Com.Paytm.Printgenerator.Page.Page;
using FontSize = Com.Paytm.Printgenerator.FontSize;
using PaytmPayments = Com.Paytm.Pos.App_invoke_sdk.PaytmPayments;
using Microsoft.Maui.Controls;

namespace MauiApp1
{
    [Activity(Theme = "@style/Maui.SplashTheme", MainLauncher = true, LaunchMode = LaunchMode.SingleTop, ConfigurationChanges = ConfigChanges.ScreenSize | ConfigChanges.Orientation | ConfigChanges.UiMode | ConfigChanges.ScreenLayout | ConfigChanges.SmallestScreenSize | ConfigChanges.Density)]
    public class MainActivity : MauiAppCompatActivity, IPrintStatusCallBack, PaytmPayments.IPaytmResult
    {
        protected override void OnCreate(Bundle? savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            //PrintDemoPage();

        }

        public void CollectPayment(string Amount, string OrderId, string PayMode)
        {
            PaytmPayments.Instance.DoPayment(this, this, int.Parse(Amount), OrderId, PayMode);
        }


        public void PrintDemoPage()
        {
            // Example of using the Printer
            try
            {
                // Create a bitmap or page to print
                Page page = GetPage();// your bitmap here
                long currentTimeMillis = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
                string requestId = currentTimeMillis.ToString();

                // Use the Printer class (it's a singleton-style class with static methods)
                //Printer.Companion companion = Printer.
                string printJobId = Printer.Print(
                    page,
                    requestId,
                    this, // Context
                    this  // PrintStatusCallBack
                );

            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine($"Printing error: {ex.Message}");
            }
        }

        private static Page GetPage()
        {
            var page = new Page();
            page.AddLine().AddElement(new TextElement("Demo Print"));
            page.AddLine().AddElement(new TextElement(false, false, FontSize.FontMedium, "Demo Print"));
            page.AddLine().AddElement(new TextElement(false, false, FontSize.FontNormal, "Demo Print", Typeface.Default, Alignment.Right));
            page.AddLine().AddElement(new TextElement(false, false, FontSize.FontNormal, "Left Aligned", Typeface.Default, Alignment.Left, 0, 1))
              .AddElement(new TextElement(false, false, FontSize.FontNormal, "Center Aligned", Typeface.Default, Alignment.Center, 0, 1))
              .AddElement(new TextElement(false, false, FontSize.FontNormal, "Right Aligned", Typeface.Default, Alignment.Right, 0, 1));
            return page;
        }



        public void OnFailure(string p0, PrintStatusEnum p1)
        {
            System.Diagnostics.Debug.WriteLine($"Print failure for request {p0}: {p1}");

        }

        public void OnPrintStarted(string p0)
        {
            System.Diagnostics.Debug.WriteLine($"Print started for request {p0}:");

        }

        public void OnSuccess(string p0)
        {
            System.Diagnostics.Debug.WriteLine($"Print success for request {p0}:");

        }

        void PaytmPayments.IPaytmResult.OnError(int requestId, string errorMsg)
        {
            System.Diagnostics.Debug.WriteLine($"Payment request failed success for request Id : {requestId}, error : {errorMsg}");
        }

        public void OnResult(int requestId, IDictionary<string, string> responseData)
        {
            var resultString = string.Join(", ", responseData.Select(kv => $"{kv.Key}: {kv.Value}"));
            System.Diagnostics.Debug.WriteLine($"Payment result request Id : {requestId}, result : {resultString}");

            // Send message to .NET MAUI
            MessagingCenter.Send<object, string>(this, "PaymentResult", resultString);
        }
    }
}
