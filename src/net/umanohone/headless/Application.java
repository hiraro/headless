package net.umanohone.headless;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.umanohone.headless.parser.CommentParser;
import net.umanohone.headless.parser.TypeParser;
import net.umanohone.headless.parser.MethodCallParser;
import net.umanohone.headless.parser.ElementParser;
import net.umanohone.headless.parser.PackageParser;
import net.umanohone.headless.utils.DbUtil;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class Application implements IApplication {

	public static final String targetProjectName;
	public static final List<String> exclusion;
	public static final List<String> inclusion;
	public static final Logger logger;
	public static final String confFile = "/Users/hiraro/Dropbox/workspace/headless/conf.properties";
	public static final Properties properties;

	static {
		properties = new Properties();
		try {
			properties.load(new FileReader(new File(confFile)));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		String propTarget = properties.getProperty("target");
		String propExclusion = properties.getProperty("exclusion");
		String propInclusion = properties.getProperty("inclusion", "*");
		targetProjectName = propTarget;
		exclusion = propExclusion != null ? Arrays.asList(propExclusion
				.split(",")) : new ArrayList<String>();
		inclusion = propInclusion != null ? Arrays.asList(propInclusion
				.split(",")) : new ArrayList<String>();
		logger = Logger.getGlobal();
		logger.setLevel(Level.WARNING);
	}

	private static IJavaProject target;
	private static ProjectWalker walker;

	public Object start(IApplicationContext context) throws Exception {
		target = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()
				.getProject(targetProjectName));
		DbUtil.initEbean(properties);
		PackageParser packageParser = new PackageParser();
		TypeParser clazzParser = new TypeParser();
		MethodCallParser methodCallParser = new MethodCallParser();
		ElementParser methodElementParser = new ElementParser();
		CommentParser commentParser = new CommentParser();
		initWalker();
		getWalker().addObserver(packageParser);
		getWalker().addObserver(clazzParser);
		getWalker().addObserver(methodCallParser);
		getWalker().addObserver(methodElementParser);
		getWalker().addObserver(commentParser);
		getWalker().execute();
		return IApplication.EXIT_OK;
	}

	public void stop() {
	}

	public static ProjectWalker getWalker() {
		return walker;
	}

	private static void initWalker() {
		walker = new ProjectWalker(target);
		walker.setExclusion(exclusion);
		walker.setInclusion(inclusion);
	}
}
