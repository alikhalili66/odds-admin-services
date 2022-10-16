package ir.khalili.products.odds.core.helper;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.odds.core.constants.AppConstants;
import ir.khalili.products.odds.core.utils.Configuration;

public abstract class HelperConnection {

	private static final Logger logger = LogManager.getLogger(HelperConnection.class);

	public void getConnection(Vertx vertx, String className, Message<Object> message) {

		JDBCClient ircJDBC = JDBCClient.createShared(vertx, Configuration.getDataBaseConfig(), AppConstants.APP_DS_ODDS);

		ircJDBC.getConnection(connection -> {

			if (connection.failed()) {
				logger.error("Unable to get connection from database:", connection.cause());
				message.fail(-12020, "امکان برقراری ارتباط با بانک اطلاعاتی نیست");
				return;
			}

			SQLConnection sqlConnection = connection.result();

			callBiz(sqlConnection, resultHandler -> {
				if (resultHandler.succeeded()) {
					logger.trace(className + ",Succeeded:" + resultHandler.result());
					message.reply(resultHandler.result());
				} else {
					JsonObject excp = JsonObject.mapFrom(resultHandler.cause());
					logger.error(className + ",Failed:" + excp);
					message.fail(excp.getInteger("resultCode"), excp.getString("resultMessage"));
				}

				sqlConnection.close(handler -> {
					if (handler.failed()) {
						logger.error("IncompleteSessionClose", handler.cause());
					}
				});
			});

		});

	}

	public void getTransactionalConnection(Vertx vertx, String className, Message<Object> message) {

		JDBCClient ircJDBC = JDBCClient.createShared(vertx, Configuration.getDataBaseConfig(), AppConstants.APP_DS_ODDS);

		ircJDBC.getConnection(connection -> {

			if (connection.failed()) {
				logger.error("Unable to get connection from database:", connection.cause());
				message.fail(-12020, "امکان برقراری ارتباط با بانک اطلاعاتی نیست");
				return;
			}

			SQLConnection sqlConnection = connection.result();

			sqlConnection.setAutoCommit(false, autoCommitHandler -> {

				if (autoCommitHandler.failed()) {
					logger.error("Unable to setAutoCommit set to false" + autoCommitHandler.cause());
					message.fail(-12020, "امکان برقراری ارتباط با بانک اطلاعاتی نیست");
					return;
				}

				callBiz(sqlConnection, resultHandler -> {

					if (resultHandler.failed()) {

						sqlConnection.rollback(rollBackHandler -> {
							if (rollBackHandler.failed()) {
								logger.error("RollBackFailed", rollBackHandler.cause());
							}

							JsonObject excp = JsonObject.mapFrom(resultHandler.cause());
							logger.error(className + ",Failed:" + excp);
							message.fail(excp.getInteger("resultCode"), excp.getString("resultMessage"));

							sqlConnection.close(handler -> {
								if (handler.failed()) {
									logger.error("IncompleteSessionClose", handler.cause());
								}
							});
						});

					} else {
						sqlConnection.commit(commitHandler -> {
							if (commitHandler.failed()) {

								logger.error("Unable to get accessQueryResult:", commitHandler.cause());

								sqlConnection.rollback(rollBackHandler -> {
									if (rollBackHandler.failed()) {
										logger.error("RollBackFailed", rollBackHandler.cause());
									}
								});

								message.fail(-100, "خطای داخلی. با راهبر سامانه تماس بگیرید.");
							} else {
								logger.trace(className + ",Succeeded:" + resultHandler.result());
								message.reply(resultHandler.result());
							}

							sqlConnection.close(handler -> {
								if (handler.failed()) {
									logger.error("IncompleteSessionClose", handler.cause());
								}
							});
						});
					}

				});
			});
		});

	}

	public abstract void callBiz(SQLConnection sqlConnection, Handler<AsyncResult<JsonObject>> resultHandler);

}
