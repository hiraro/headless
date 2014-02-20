package net.umanohone.headless.utils;

import java.util.Properties;

import net.umanohone.headless.models.Type;
import net.umanohone.headless.models.Element;
import net.umanohone.headless.models.Method;
import net.umanohone.headless.models.MethodCallMap;
import net.umanohone.headless.models.ElementLocation;
import net.umanohone.headless.models.Package;
import net.umanohone.headless.models.TypeHierarchie;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;

public class DbUtil {
	private DbUtil() {
	}

	public static EbeanServer server = null;

	public static void initEbean(Properties p) {
		ServerConfig config = new ServerConfig();
		config.setName("conf");

		DataSourceConfig mysqlDb = new DataSourceConfig();
		mysqlDb.setDriver(p.getProperty("db.driver"));
		mysqlDb.setUsername(p.getProperty("db.username"));
		mysqlDb.setPassword(p.getProperty("db.password"));
		mysqlDb.setUrl(p.getProperty("db.url"));

		config.setDataSourceConfig(mysqlDb);

		config.setDdlGenerate(true);
		config.setDdlRun(true);

		config.setDefaultServer(true);
		config.setRegister(true);

		config.addClass(Type.class);
		config.addClass(Method.class);
		config.addClass(Element.class);
		config.addClass(Package.class);
		config.addClass(MethodCallMap.class);
		config.addClass(ElementLocation.class);
		config.addClass(TypeHierarchie.class);

		server = EbeanServerFactory.create(config);
	}
}