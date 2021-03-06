package org.onetwo.dbm.support;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.db.DataBase;
import org.onetwo.common.db.filter.annotation.DataQueryFilterListener;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.dialet.DBDialect;
import org.onetwo.dbm.dialet.DbmetaFetcher;
import org.onetwo.dbm.dialet.DefaultDatabaseDialetManager;
import org.onetwo.dbm.dialet.MySQLDialect;
import org.onetwo.dbm.dialet.OracleDialect;
import org.onetwo.dbm.dialet.AbstractDBDialect.DBMeta;
import org.onetwo.dbm.jdbc.JdbcResultSetGetter;
import org.onetwo.dbm.jdbc.JdbcStatementParameterSetter;
import org.onetwo.dbm.jdbc.SpringJdbcResultSetGetter;
import org.onetwo.dbm.jdbc.SpringStatementParameterSetter;
import org.onetwo.dbm.jdbc.mapper.JFishRowMapperFactory;
import org.onetwo.dbm.jdbc.mapper.RowMapperFactory;
import org.onetwo.dbm.jpa.JFishSequenceNameManager;
import org.onetwo.dbm.jpa.JPAMappedEntryBuilder;
import org.onetwo.dbm.mapping.DataBaseConfig;
import org.onetwo.dbm.mapping.DbmTypeMapping;
import org.onetwo.dbm.mapping.DefaultDataBaseConfig;
import org.onetwo.dbm.mapping.EntityValidator;
import org.onetwo.dbm.mapping.JFishMappedEntryBuilder;
import org.onetwo.dbm.mapping.MappedEntryBuilder;
import org.onetwo.dbm.mapping.MappedEntryManager;
import org.onetwo.dbm.mapping.MutilMappedEntryManager;
import org.onetwo.dbm.query.JFishSQLSymbolManagerImpl;
import org.springframework.util.Assert;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class SimpleDbmInnserServiceRegistry {
	
	final private Map<String, Object> services = Maps.newConcurrentMap();

	private DBDialect dialect;
	private MappedEntryManager mappedEntryManager;
	private SQLSymbolManager sqlSymbolManager;
	private SequenceNameManager sequenceNameManager;
	private DefaultDatabaseDialetManager databaseDialetManager;
	private DataBaseConfig dataBaseConfig;
	private RowMapperFactory rowMapperFactory;
	private EntityValidator entityValidator;
	private JdbcStatementParameterSetter jdbcParameterSetter;
	private JdbcResultSetGetter jdbcResultSetGetter;
	private DbmTypeMapping typeMapping;
	
	public void initialize(DataSource dataSource, String[] packagesToScan){
		if(dataBaseConfig==null){
			dataBaseConfig = new DefaultDataBaseConfig();
		}
		if(jdbcParameterSetter==null){
			this.jdbcParameterSetter = new SpringStatementParameterSetter();
		}
		if(jdbcResultSetGetter==null){
			this.jdbcResultSetGetter = new SpringJdbcResultSetGetter();
		}
		if(typeMapping==null){
			this.typeMapping = new DbmTypeMapping();
		}
		if(databaseDialetManager==null){
			this.databaseDialetManager = new DefaultDatabaseDialetManager();
			this.databaseDialetManager.register(DataBase.MySQL.getName(), new MySQLDialect());
			this.databaseDialetManager.register(DataBase.Oracle.getName(), new OracleDialect());
		}
		
		
//		super.initDao();
		if(this.dialect==null){
			DBMeta dbmeta = DbmetaFetcher.create(dataSource).getDBMeta();
//			this.dialect = JFishSpringUtils.getMatchDBDiaclet(applicationContext, dbmeta);
			this.dialect = this.databaseDialetManager.getRegistered(dbmeta.getDbName());
			if (this.dialect == null) {
				throw new IllegalArgumentException("'dialect' is required");
			}
//			LangUtils.cast(dialect, InnerDBDialet.class).setDbmeta(dbmeta);
			this.dialect.getDbmeta().setVersion(dbmeta.getVersion());
			this.dialect.initialize();
		}
		
		//mappedEntryManager
		if(mappedEntryManager==null){
			MutilMappedEntryManager _mappedEntryManager = new MutilMappedEntryManager();
//			this.mappedEntryManager.initialize();
			
			List<MappedEntryBuilder> builders = LangUtils.newArrayList();
			MappedEntryBuilder builder = new JFishMappedEntryBuilder(this);
			builder.initialize();
			builders.add(builder);
			
			builder = new JPAMappedEntryBuilder(this);
			builder.initialize();
			builders.add(builder);
//			this.mappedEntryBuilders = ImmutableList.copyOf(builders);
			_mappedEntryManager.setMappedEntryBuilder(ImmutableList.copyOf(builders));
			this.mappedEntryManager = _mappedEntryManager;
			
		}
		if(ArrayUtils.isNotEmpty(packagesToScan)){
			mappedEntryManager.scanPackages(packagesToScan);
		}
		
		//init sql symbol
		if(sqlSymbolManager==null){
			JFishSQLSymbolManagerImpl newSqlSymbolManager = JFishSQLSymbolManagerImpl.create();
//			newSqlSymbolManager.setDialect(dialect);
			newSqlSymbolManager.setMappedEntryManager(mappedEntryManager);
			newSqlSymbolManager.setListeners(Arrays.asList(new DataQueryFilterListener()));
			this.sqlSymbolManager = newSqlSymbolManager;
		}
		
//		this.mappedEntryManager = SpringUtils.getHighestOrder(applicationContext, MappedEntryManager.class);
		this.rowMapperFactory = new JFishRowMapperFactory(mappedEntryManager, jdbcResultSetGetter);

		if(this.sequenceNameManager==null){
			this.sequenceNameManager = new JFishSequenceNameManager();
		}
	}
	

	public DBDialect getDialect() {
		return dialect;
	}


	public void setJdbcParameterSetter(JdbcStatementParameterSetter jdbcParameterSetter) {
		this.jdbcParameterSetter = jdbcParameterSetter;
	}


	public JdbcStatementParameterSetter getJdbcParameterSetter() {
		return jdbcParameterSetter;
	}


	public MappedEntryManager getMappedEntryManager() {
		return mappedEntryManager;
	}

	public SQLSymbolManager getSqlSymbolManager() {
		return sqlSymbolManager;
	}

	public SequenceNameManager getSequenceNameManager() {
		return sequenceNameManager;
	}

	public DefaultDatabaseDialetManager getDatabaseDialetManager() {
		return databaseDialetManager;
	}

	public DataBaseConfig getDataBaseConfig() {
		return dataBaseConfig;
	}

	public RowMapperFactory getRowMapperFactory() {
		return rowMapperFactory;
	}

	public <T> T getService(Class<T> clazz) {
		Assert.notNull(clazz);
		return clazz.cast(getService(clazz.getName()));
	}

	@SuppressWarnings("unchecked")
	public <T> T getService(String name) {
		Assert.hasText(name);
		return (T) services.get(name);
	}

	public <T> SimpleDbmInnserServiceRegistry register(T service) {
		return register(service.getClass().getName(), service);
	}

	public <T> SimpleDbmInnserServiceRegistry register(String name, T service) {
		Assert.hasText(name);
		Assert.notNull(service);
		services.put(name, service);
		return this;
	}

	public EntityValidator getEntityValidator() {
		return entityValidator;
	}


	public void setEntityValidator(EntityValidator entityValidator) {
		this.entityValidator = entityValidator;
	}


	public DbmTypeMapping getTypeMapping() {
		return typeMapping;
	}


	public void setTypeMapping(DbmTypeMapping typeMapping) {
		this.typeMapping = typeMapping;
	}
	

}
