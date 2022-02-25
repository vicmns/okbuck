package com.uber.okbuck.extension;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.Optional;

@SuppressWarnings("unused")
public class OkBuckExtension {

  // Forked buck which works on bigsur
  // https://github.com/raviagarwal7/buck/commits/8369cecf8b4a8d628f7852f0030587944f01bc19
  private static final String DEFAULT_BUCK_BINARY_REPO = "com.github.raviagarwal7:buck";
  private static final String DEFAULT_BUCK_BINARY_SHA = "8369cecf8b4a8d628f7852f0030587944f01bc19";

  /** Build Tools Version */
  @Input public String buildToolVersion = "28.0.2";

  /** Android target sdk version */
  @Input public String target = "android-28";

  /** Annotation processor classes of project dependencies */
  @Input public Map<String, List<String>> annotationProcessors = new HashMap<>();

  /** LinearAllocHardLimit used for multi-dex support. */
  @Input public Map<String, Integer> linearAllocHardLimit = new HashMap<>();

  /** Primary dex class patterns. */
  @Input public Map<String, List<String>> primaryDexPatterns = new HashMap<>();

  /** Whether to enable exopackage. */
  @Input public Map<String, Boolean> exopackage = new HashMap<>();

  /** Exopackage lib dependencies. */
  @Input public Map<String, List<String>> appLibDependencies = new HashMap<>();

  /** Proguard mapping file applied via apply mapping */
  @Input public Map<String, File> proguardMappingFile = new HashMap<>();

  /** List of build types/variant names for which to exclude generating lint rules */
  @Input public Map<String, List<String>> lintExclude = new HashMap<>();

  /** List of build types/variant names for which to exclude generating test rules */
  @Input public Map<String, List<String>> testExclude = new HashMap<>();

  /** Set of projects to generate buck configs for. Default is all subprojects of root project. */
  @Internal public Set<Project> buckProjects;

  /** Name of the build file where generated build rules will be written. */
  @Input public String buildFileName = "BUCK";

  /** Whether to add OkBuckBuckConfig to top level .buckconfig */
  @Input public boolean okBuckBuckConfig = true;

  /** Extra buck options */
  @Input public Map<String, Map<String, Collection<String>>> extraBuckOpts = new HashMap<>();

  /**
   * Set to use buck's resource_union behavior with the original package name or the defined by
   * {@link OkBuckExtension#resourceUnionPackage}
   */
  @Input public boolean resourceUnion;

  /** Set to use buck's resource_union behavior with an specific package name */
  @Nullable @Input public String resourceUnionPackage;

  /** Whether to generate android_build_config rules for library projects */
  @Input public boolean libraryBuildConfig = true;

  /** List of exclude patterns for resources to be processed by aapt */
  @Input public Set<String> excludeResources = new HashSet<>();

  /**
   * Additional dependency caches. Every value "entry" will create a new configuration
   * "entryExtraDepCache" that can be used to fetch and cache dependencies. the boolean defines
   * weather a prebuilt rule needs to be skipped or not. { "tools": true } skips prebuilt rule for
   * all tools dependencies
   */
  @Input public Map<String, Boolean> extraDepCachesMap = new HashMap<>();

  /** Forces okbuck to fail if the project is using dynamic or snapshot dependencies */
  @Input public boolean failOnChangingDependencies = false;

  /** Controls output of legacy annotation processor dependencies in generated build files */
  @Input public boolean legacyAnnotationProcessorSupport = true;

  /** The prebuilt buck binary to use */
  @Input
  @Optional
  public String buckBinary = DEFAULT_BUCK_BINARY_REPO + ":" + DEFAULT_BUCK_BINARY_SHA + "@pex";

  /** The prebuilt buck binary to use with java 11 */
  @Input
  public String buckBinaryJava11 =
      DEFAULT_BUCK_BINARY_REPO + ":" + DEFAULT_BUCK_BINARY_SHA + ":java11@pex";

  @Internal private WrapperExtension wrapperExtension = new WrapperExtension();
  @Internal private KotlinExtension kotlinExtension;
  @Internal private ScalaExtension scalaExtension = new ScalaExtension();
  @Internal private IntellijExtension intellijExtension = new IntellijExtension();
  @Internal
  private ExperimentalExtension experimentalExtension = new ExperimentalExtension();
  @Internal private TestExtension testExtension = new TestExtension();
  @Internal private TransformExtension transformExtension = new TransformExtension();
  @Internal private LintExtension lintExtension;
  @Internal private JetifierExtension jetifierExtension;
  @Internal
  private ExternalDependenciesExtension externalDependenciesExtension =
      new ExternalDependenciesExtension();
  @Internal private VisibilityExtension visibilityExtension = new VisibilityExtension();
  @Internal private RuleOverridesExtension ruleOverridesExtension;

  public OkBuckExtension(Project project) {
    buckProjects = project.getSubprojects();
    kotlinExtension = new KotlinExtension(project);
    lintExtension = new LintExtension(project);
    jetifierExtension = new JetifierExtension(project);
    ruleOverridesExtension = new RuleOverridesExtension(project);
  }

  public void wrapper(Action<WrapperExtension> container) {
    container.execute(wrapperExtension);
  }

  public WrapperExtension getWrapperExtension() {
    return wrapperExtension;
  }

  public void kotlin(Action<KotlinExtension> container) {
    container.execute(kotlinExtension);
  }

  public KotlinExtension getKotlinExtension() {
    return kotlinExtension;
  }

  public void scala(Action<ScalaExtension> container) {
    container.execute(scalaExtension);
  }

  public ScalaExtension getScalaExtension() {
    return scalaExtension;
  }

  public void intellij(Action<IntellijExtension> container) {
    container.execute(intellijExtension);
  }

  public IntellijExtension getIntellijExtension() {
    return intellijExtension;
  }

  public void experimental(Action<ExperimentalExtension> container) {
    container.execute(experimentalExtension);
  }

  public ExperimentalExtension getExperimentalExtension() {
    return experimentalExtension;
  }

  public void test(Action<TestExtension> container) {
    container.execute(testExtension);
  }

  public TestExtension getTestExtension() {
    return testExtension;
  }

  public void lint(Action<LintExtension> container) {
    container.execute(lintExtension);
  }

  public LintExtension getLintExtension() {
    return lintExtension;
  }

  public void jetifier(Action<JetifierExtension> container) {
    container.execute(jetifierExtension);
  }

  public JetifierExtension getJetifierExtension() {
    return jetifierExtension;
  }

  public void transform(Action<TransformExtension> container) {
    container.execute(transformExtension);
  }

  public TransformExtension getTransformExtension() {
    return transformExtension;
  }

  public void externalDependencies(Action<ExternalDependenciesExtension> container) {
    container.execute(externalDependenciesExtension);
  }

  public ExternalDependenciesExtension getExternalDependenciesExtension() {
    return externalDependenciesExtension;
  }

  public void visibility(Action<VisibilityExtension> container) {
    container.execute(visibilityExtension);
  }

  public VisibilityExtension getVisibilityExtension() {
    return visibilityExtension;
  }

  public void ruleOverrides(Action<RuleOverridesExtension> container) {
    container.execute(ruleOverridesExtension);
  }

  public RuleOverridesExtension getRuleOverridesExtension() {
    return ruleOverridesExtension;
  }

  public boolean useResourceUnion() {
    return resourceUnionPackage != null || resourceUnion;
  }

  public Map<String, List<String>> getAnnotationProcessors() {
    return annotationProcessors;
  }

  public String getBuildToolVersion() {
    return buildToolVersion;
  }

  public String getTarget() {
    return target;
  }

  public Map<String, Integer> getLinearAllocHardLimit() {
    return linearAllocHardLimit;
  }

  public Map<String, List<String>> getPrimaryDexPatterns() {
    return primaryDexPatterns;
  }

  public Map<String, Boolean> getExopackage() {
    return exopackage;
  }

  public Map<String, File> getProguardMappingFile() {
    return proguardMappingFile;
  }

  public Map<String, List<String>> getLintExclude() {
    return lintExclude;
  }

  public Map<String, List<String>> getTestExclude() {
    return testExclude;
  }

  public String getBuildFileName() {
    return buildFileName;
  }

  public boolean isOkBuckBuckConfig() {
    return okBuckBuckConfig;
  }

  public Map<String, Map<String, Collection<String>>> getExtraBuckOpts() {
    return extraBuckOpts;
  }

  public boolean isResourceUnion() {
    return resourceUnion;
  }

  @Nullable
  public String getResourceUnionPackage() {
    return resourceUnionPackage;
  }

  public boolean isLibraryBuildConfig() {
    return libraryBuildConfig;
  }

  public Set<String> getExcludeResources() {
    return excludeResources;
  }

  public Map<String, List<String>> getAppLibDependencies() {
    return appLibDependencies;
  }

  public Set<Project> getBuckProjects() {
    return buckProjects;
  }

  public String getBuckBinary() {
    return buckBinary;
  }

  public String getBuckBinaryJava11() {
    return buckBinaryJava11;
  }

  public Map<String, Boolean> getExtraDepCachesMap() {
    return extraDepCachesMap;
  }

  public boolean isFailOnChangingDependencies() {
    return failOnChangingDependencies;
  }

  public boolean isLegacyAnnotationProcessorSupport() {
    return legacyAnnotationProcessorSupport;
  }
}
