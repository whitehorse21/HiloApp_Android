package com.hiloipa.app.hilo.ui.auth


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiloipa.app.hilo.R
import kotlinx.android.synthetic.main.fragment_terms.*


/**
 * A simple [Fragment] subclass.
 */
val termsText = "<p style=\"text-align: center;\">Terms and Conditions<br />Updated: February 29, 2016</p>\n" +
        "<p style=\"text-align: center;\">Please read these Terms and Conditions (\"Terms\", \"Terms and Conditions\") carefully before using the https://hiloipa.com website (the \"Service\") operated by Hilo LLC (\"us\", \"we\", or \"our\").</p>\n" +
        "<p style=\"text-align: center;\">Your access to and use of the Service is conditioned upon your acceptance of and compliance with these Terms. These Terms apply to all visitors, users and others who wish to access or use the Service.</p>\n" +
        "<p style=\"text-align: center;\">By accessing or using the Service you agree to be bound by these Terms. If you disagree with any part of the terms then you do not have permission to access the Service.</p>\n" +
        "<p style=\"text-align: center;\"><br />Communications <br />By creating an Account on our service, you agree to subscribe to newsletters, marketing or promotional materials and other information we may send. However, you may opt out of receiving any, or all, of these communications from us by following the unsubscribe link or instructions provided in any email we send.</p>\n" +
        "<p style=\"text-align: center;\">Purchases <br />If you wish to purchase any product or service made available through the Service (\"Purchase\"), you may be asked to supply certain information relevant to your Purchase including, without limitation, your credit card number, the expiration date of your credit card, your billing address, and your shipping information.</p>\n" +
        "<p style=\"text-align: center;\">You represent and warrant that: (i) you have the legal right to use any credit card(s) or other payment method(s) in connection with any Purchase; and that (ii) the information you supply to us is true, correct and complete.</p>\n" +
        "<p style=\"text-align: center;\">The service may employ the use of third party services for the purpose of facilitating payment and the completion of Purchases. By submitting your information, you grant us the right to provide the information to these third parties subject to our Privacy Policy.</p>\n" +
        "<p style=\"text-align: center;\">We reserve the right to refuse or cancel your order at any time for reasons including but not limited to: product or service availability, errors in the description or price of the product or service, error in your order or other reasons.</p>\n" +
        "<p style=\"text-align: center;\">We reserve the right to refuse or cancel your order if fraud or an unauthorized or illegal transaction is suspected.</p>\n" +
        "<p style=\"text-align: center;\"><br />Availability, Errors and Inaccuracies<br />We are constantly updating product and service offerings on the Service. We may experience delays in updating information on the Service and in our advertising on other web sites. The information found on the Service may contain errors or inaccuracies and may not be complete or current. Products or services may be mispriced, described inaccurately, or unavailable on the Service and we cannot guarantee the accuracy or completeness of any information found on the Service.</p>\n" +
        "<p style=\"text-align: center;\">We therefore reserve the right to change or update information and to correct errors, inaccuracies, or omissions at any time without prior notice.</p>\n" +
        "<p style=\"text-align: center;\"><br />Contests, Sweepstakes and Promotions <br />Any contests, sweepstakes or other promotions (collectively, \"Promotions\") made available through the Service may be governed by rules that are separate from these Terms &amp; Conditions. If you participate in any Promotions, please review the applicable rules as well as our Privacy Policy. If the rules for a Promotion conflict with these Terms and Conditions, the Promotion rules will apply.</p>\n" +
        "<p style=\"text-align: center;\"><br />Content <br />Our Service allows you to post, link, store, share and otherwise make available certain information, text, graphics, videos, or other material (\"Content\"). You are responsible for the Content that you post on or through the Service, including its legality, reliability, and appropriateness.</p>\n" +
        "<p style=\"text-align: center;\">By posting Content on or through the Service, You represent and warrant that: (i) the Content is yours (you own it) and/or you have the right to use it and the right to grant us the rights and license as provided in these Terms, and (ii) that the posting of your Content on or through the Service does not violate the privacy rights, publicity rights, copyrights, contract rights or any other rights of any person or entity. We reserve the right to terminate the account of anyone found to be infringing on a copyright.</p>\n" +
        "<p style=\"text-align: center;\">You retain any and all of your rights to any Content you submit, post or display on or through the Service and you are responsible for protecting those rights. We take no responsibility and assume no liability for Content you or any third party posts on or through the Service. However, by posting Content using the Service you grant us the right and license to use, modify, publicly perform, publicly display, reproduce, and distribute such Content on and through the Service.</p>\n" +
        "<p style=\"text-align: center;\">Hilo LLC has the right but not the obligation to monitor and edit all Content provided by users.</p>\n" +
        "<p style=\"text-align: center;\">In addition, Content found on or through this Service are the property of Hilo LLC or used with permission. You may not distribute, modify, transmit, reuse, download, repost, copy, or use said Content, whether in whole or in part, for commercial purposes or for personal gain, without express advance written permission from us.</p>\n" +
        "<p style=\"text-align: center;\"><br />Accounts <br />When you create an account with us, you guarantee that you are above the age of 18, and that the information you provide us is accurate, complete, and current at all times. Inaccurate, incomplete, or obsolete information may result in the immediate termination of your account on the Service.</p>\n" +
        "<p style=\"text-align: center;\">You are responsible for maintaining the confidentiality of your account and password, including but not limited to the restriction of access to your computer and/or account. You agree to accept responsibility for any and all activities or actions that occur under your account and/or password, whether your password is with our Service or a third-party service. You must notify us immediately upon becoming aware of any breach of security or unauthorized use of your account.</p>\n" +
        "<p style=\"text-align: center;\">You may not use as a username the name of another person or entity or that is not lawfully available for use, a name or trademark that is subject to any rights of another person or entity other than you, without appropriate authorization. You may not use as a username any name that is offensive, vulgar or obscene.</p>\n" +
        "<p style=\"text-align: center;\">We reserve the right to refuse service, terminate accounts, remove or edit content, or cancel orders in our sole discretion.</p>\n" +
        "<p style=\"text-align: center;\"><br />Links To Other Web Sites <br />Our Service may contain links to third party web sites or services that are not owned or controlled by Hilo LLC.</p>\n" +
        "<p style=\"text-align: center;\">Hilo LLC has no control over, and assumes no responsibility for the content, privacy policies, or practices of any third party web sites or services. We do not warrant the offerings of any of these entities/individuals or their websites.</p>\n" +
        "<p style=\"text-align: center;\">You acknowledge and agree that Hilo LLC shall not be responsible or liable, directly or indirectly, for any damage or loss caused or alleged to be caused by or in connection with use of or reliance on any such content, goods or services available on or through any such third party web sites or services.</p>\n" +
        "<p style=\"text-align: center;\">We strongly advise you to read the terms and conditions and privacy policies of any third party web sites or services that you visit.</p>\n" +
        "<p style=\"text-align: center;\"><br />Termination <br />We may terminate or suspend your account and bar access to the Service immediately, without prior notice or liability, under our sole discretion, for any reason whatsoever and without limitation, including but not limited to a breach of the Terms.</p>\n" +
        "<p style=\"text-align: center;\">If you wish to terminate your account, you may simply discontinue using the Service.</p>\n" +
        "<p style=\"text-align: center;\">All provisions of the Terms which by their nature should survive termination shall survive termination, including, without limitation, ownership provisions, warranty disclaimers, indemnity and limitations of liability.</p>\n" +
        "<p style=\"text-align: center;\"><br />Indemnification <br />You agree to defend, indemnify and hold harmless Hilo LLC and its licensee and licensors, and their employees, contractors, agents, officers and directors, from and against any and all claims, damages, obligations, losses, liabilities, costs or debt, and expenses (including but not limited to attorney's fees), resulting from or arising out of a) your use and access of the Service, by you or ...</p>"

class TermsFragment : Fragment() {

    companion object {
        fun newInstance(): TermsFragment {
            val args = Bundle()
            val fragment = TermsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater!!.inflate(R.layout.fragment_terms, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeBtn.setOnClickListener { activity.onBackPressed() }
        termsLabel.text = Html.fromHtml(termsText)
    }
}
