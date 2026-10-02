import hudson.security.FullControlOnceLoggedInAuthorizationStrategy
import hudson.security.HudsonPrivateSecurityRealm
import jenkins.model.Jenkins

def jenkins = Jenkins.get()
def realm = jenkins.securityRealm
if (!(realm instanceof HudsonPrivateSecurityRealm) || realm.getAllUsers().isEmpty()) {
    def newRealm = new HudsonPrivateSecurityRealm(false)
    newRealm.createAccount('admin', 'admin')
    jenkins.setSecurityRealm(newRealm)
    def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
    strategy.setAllowAnonymousRead(false)
    jenkins.setAuthorizationStrategy(strategy)
    jenkins.save()
    println '==> security: admin user created'
}
