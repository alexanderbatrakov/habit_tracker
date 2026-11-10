import hudson.model.Node
import hudson.slaves.DumbSlave
import hudson.slaves.JNLPLauncher
import hudson.slaves.RetentionStrategy
import jenkins.model.Jenkins

def jenkins = Jenkins.get()
def agentName = 'android-agent'

if (jenkins.getNode(agentName) == null) {
    def node = new DumbSlave(agentName, '/home/jenkins', new JNLPLauncher())
    node.nodeDescription = 'Android build agent (docker/android-agent)'
    node.mode = Node.Mode.EXCLUSIVE
    node.labelString = 'android'
    node.numExecutors = 1
    node.retentionStrategy = new RetentionStrategy.Always()
    jenkins.addNode(node)
    println "==> agent: node '${agentName}' created with label 'android'"
}

def computer = jenkins.getNode(agentName)?.toComputer()
if (computer != null) {
    def secretFile = new File(jenkins.rootDir, 'agent.secret')
    secretFile.text = computer.jnlpMac
    println "==> agent: secret written to ${secretFile.path}"
} else {
    println "==> agent: WARNING computer not available yet, agent will use UI secret fallback"
}
