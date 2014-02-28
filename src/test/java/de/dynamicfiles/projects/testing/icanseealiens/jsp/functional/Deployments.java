package de.dynamicfiles.projects.testing.icanseealiens.jsp.functional;

import java.io.File;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;

/**
 * To have lean test-classes you should have all deployments at a single position.
 *
 * @author Danny Althoff
 */
public class Deployments {

    private static final String WEBAPP_SRC = "src/main/webapp";

    public static void addCDI(WebArchive archive) {
        // CDI in WAR -> WEB-INF
        archive.addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF/beans.xml"), "beans.xml");
    }

    public static void addCDI(JavaArchive archive) {
        // CDI in JAR -> META-INF
        archive.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    public static void addAllClasses(WebArchive archive) {
        archive.addPackage(de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoPasswordProvided.class.getPackage());
        archive.addPackage(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.BillboardEntryManager.class.getPackage());
        archive.addPackage(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.annotation.Transactional.class.getPackage());
        archive.addPackage(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.interceptor.TransactionInterceptor.class.getPackage());
        archive.addPackage(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry.class.getPackage());
        archive.addPackage(de.dynamicfiles.projects.testing.icanseealiens.jsp.observers.UserObserver.class.getPackage());
        archive.addPackage(de.dynamicfiles.projects.testing.icanseealiens.jsp.producer.UserSessionProducerDisposer.class.getPackage());
        archive.addPackage(de.dynamicfiles.projects.testing.icanseealiens.jsp.producer.slsb.BillboardEntryManagerSLSB.class.getPackage());
        archive.addPackage(de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.Current.class.getPackage());
        archive.addPackage(de.dynamicfiles.projects.testing.icanseealiens.jsp.session.UserSession.class.getPackage());
    }

    public static void addPersistence(WebArchive archive) {
        archive.addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml");
    }

    public static void addDependencies(WebArchive archive, String dependencySelector) {
        MavenResolverSystem resolver = Maven.resolver();
        PomEquippedResolveStage pom = resolver.loadPomFromFile("pom.xml");
        archive.addAsLibraries(pom.resolve(dependencySelector).withTransitivity().asFile());
    }

    public static void addBaseWebFiles(WebArchive archive) {
        // gather all web-resources (found here: http://arquillian.org/guides/functional_testing_using_graphene/ )
        archive.merge(ShrinkWrap.create(GenericArchive.class)
                .as(ExplodedImporter.class)
                .importDirectory(WEBAPP_SRC).as(GenericArchive.class),
                "/", Filters.include(".*\\.js"));

        archive.merge(ShrinkWrap.create(GenericArchive.class)
                .as(ExplodedImporter.class)
                .importDirectory(WEBAPP_SRC).as(GenericArchive.class),
                "/", Filters.include(".*\\.css"));

        archive.merge(ShrinkWrap.create(GenericArchive.class)
                .as(ExplodedImporter.class)
                .importDirectory(WEBAPP_SRC).as(GenericArchive.class),
                "/", Filters.include(".*\\.woff"));

        archive.merge(ShrinkWrap.create(GenericArchive.class)
                .as(ExplodedImporter.class)
                .importDirectory(WEBAPP_SRC).as(GenericArchive.class),
                "/", Filters.include(".*\\.ttf"));

        archive.merge(ShrinkWrap.create(GenericArchive.class)
                .as(ExplodedImporter.class)
                .importDirectory(WEBAPP_SRC).as(GenericArchive.class),
                "/", Filters.include(".*\\.svg"));

        archive.merge(ShrinkWrap.create(GenericArchive.class)
                .as(ExplodedImporter.class)
                .importDirectory(WEBAPP_SRC).as(GenericArchive.class),
                "/", Filters.include(".*\\.eot"));

        archive.addAsWebResource(new File("src/main/webapp/_footer.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/_header.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/_navigation.jsp"));
        archive.addAsWebInfResource(new File("src/main/webapp/WEB-INF/web.xml"));
    }
}
