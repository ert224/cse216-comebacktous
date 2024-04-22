import 'package:google_sign_in/google_sign_in.dart';

class GoogleSignInApi {
    // static final _clientIDWeb =' 72850831242-dc9tqvcge6iqhjmr6bdjl0uq435asqrg.apps.googleusercontent.com';
    // static final _clientIDWeb = '72850831242-dc9tqvcge6iqhjmr6bdjl0uq435asqrg.apps.googleusercontent.com';
  static final _clientIDWeb ='1002677142862-1euvate9mnntqp0r5fme9s2i3mp56e0g.apps.googleusercontent.com';
  static final _googleSignIn = GoogleSignIn(clientId: _clientIDWeb);
  static Future<GoogleSignInAccount?> login() => _googleSignIn.signIn();
  static Future logout() => _googleSignIn.disconnect();
}
