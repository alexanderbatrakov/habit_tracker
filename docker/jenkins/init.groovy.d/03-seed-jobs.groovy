import com.cloudbees.hudson.plugins.folder.Folder
import hudson.plugins.git.BranchSpec
import hudson.plugins.git.GitSCM
import hudson.plugins.git.UserRemoteConfig
import hudson.triggers.TimerTrigger
import jenkins.model.Jenkins
import org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition
import org.jenkinsci.plugins.workflow.job.WorkflowJob

def jenkins = Jenkins.get()
def repoUrl = 'file:///workspace/repo'
def folderName = 'habit_tracker'

def folder = jenkins.getItem(folderName)
if (folder == null) {
    folder = jenkins.createProject(Folder.class, folderName)
}

def jobs = [
    [name: 'main',       branch: '*/main',     nightly: false, description: 'main: full flow (tests + lint + debug/release APK)'],
    [name: 'feature',    branch: '*/ui_tests', nightly: false, description: 'feature-ветка: unit tests + debug APK'],
    [name: 'nightly',    branch: '*/main',     nightly: true,  description: 'ночной benchmark-билд с gradle-profiler (cron H 2 * * *)'],
]

jobs.each { cfg ->
    def job = folder.getItem(cfg.name) as WorkflowJob
    if (job == null) {
        job = folder.createProject(WorkflowJob.class, cfg.name)
    }
    def scm = new GitSCM(
        [new UserRemoteConfig(repoUrl, null, null, null)],
        [new BranchSpec(cfg.branch)],
        false,
        Collections.emptyList(),
        Collections.emptyList(),
        null,
        null
    )
    job.definition = new CpsScmFlowDefinition(scm, 'Jenkinsfile')
    job.description = cfg.description
    new ArrayList(job.getTriggers().keySet()).each { t -> job.removeTrigger(t) }
    if (cfg.nightly) {
        job.addTrigger(new TimerTrigger('H 2 * * *'))
    }
    job.save()
    println "==> seed: job '${folderName}/${cfg.name}' (${cfg.branch})"
}
