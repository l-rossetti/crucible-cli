package com.loquatic.crucible.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.loquatic.crucible.cli.actions.IAction;
import com.loquatic.crucible.json.IProtocolHandler;
import com.loquatic.crucible.json.JsonHandler;
import com.loquatic.crucible.util.PropertiesHandler;

/**
 * Leverages the Apache Commons CLI API to handle mapping arguments.
 * <p>
 * If a later date the decision is made to use this outside of RCP or 
 * a third party wishes to integrate this code into another tool, 
 * this class would be the best entry point.
 * 
 * @author jsvede@yahoo.com
 * 
 */
public class UserInputProcessor {

	private String name;
	private IProtocolHandler handler ;

	/**
	 * Pass in the name of this app instance.
	 * 
	 * @param appName
	 */
	public UserInputProcessor(String appName) {
		name = appName;
		setProtocolHandler( new JsonHandler() ) ;
	}
	
	public void setProtocolHandler( IProtocolHandler myHandler ) {
		handler = myHandler ;
	}

	public void process(String[] args) throws ParseException {
		Options options = new Options();

		// Global options - available to all IAction instances.
		options.addOption( CommandLineOption.ACTION.getName(), true, "Required; the action to be performed.");
		options.addOption( CommandLineOption.USERNAME.getName(),
						            true,
						           "The Crucible username under which to " +
								       "perform this action; required if the app " +
								       "hasn't stored an authentication token.");
		options.addOption( CommandLineOption.PASSWORD.getName(), 
				               true,
				               "The password associated with the username provided.");
		options.addOption( CommandLineOption.CACHE_AUTH.getName(), 
				               true,
				               "defaults to false; when set to true the app will " +
						           "store the authToken for future. When the authToken is " +
						           "stored, the user won't need to provide username " +
						           "and password.");
		options.addOption( CommandLineOption.CONFIG.getName(),
						            true,
						            "defines the path to the configuration file. A " +
						            "configuration file defines the URL and context for " +
						            "the Crucible instance. The default file is ~/crucible-cli.properties");
		options.addOption( CommandLineOption.HELP.getName(), 
				               false, 
				               "Print help information.");
		
		ActionDispatcher dispatcher = new ActionDispatcher( handler );
		
		dispatcher.addOptionsForActions( options ) ;
		
		if (args == null || (args != null && args.length == 0)) {
			printHelp( options, dispatcher ) ;
		} else {
			CommandLineParser parser = new GnuParser();
			CommandLine commandLine = parser.parse( options, args, false ) ;

			if (commandLine.hasOption("help") && !commandLine.hasOption( "action" ) ) {
				printHelp( options, dispatcher ) ;
			} else if (commandLine.hasOption("help") && commandLine.hasOption( "action" ) ) {
				String option = commandLine.getOptionValue( "action" ) ;
				printHelp( options, dispatcher, option ) ;
			} else if (!commandLine.hasOption("action")) {
				System.out.println("--action is a required argument.");
				System.exit(0);
			} else {
				String configFilePath = commandLine.getOptionValue("config");
				if( configFilePath == null ) {
					// assume a default of ~/crucible-cli.properties
					String homeDir = System.getProperty( "user.home" ) ;
					String fileName = "crucible-cli.properties" ;
					StringBuilder sb = new StringBuilder() ;
					sb.append( homeDir ).append( File.separator ).append( fileName ) ;
					configFilePath = sb.toString() ;
				}
				Properties configProps = null;
				try {
					configProps = PropertiesHandler.load(configFilePath);
				} catch (FileNotFoundException e) {
					
					System.out.println("The default configuration file: "
						+ configFilePath + " is not present and you " +
						"did not specify an alternate location using the " +
						"--config option.");
					System.exit(0);
				} catch (IOException e) {
					System.out.println("Unable to load the configuration file: "
							+ configFilePath);
					e.printStackTrace();
					System.exit(0);
				}
				

				String action = commandLine.getOptionValue("action");
				Action userAction = Action.findByName(action);
				if (userAction == null) {
					System.out.println("Action: " + action
							+ " is invalid. Please try again.");
					HelpFormatter helpFormatter = new HelpFormatter();
					helpFormatter.printHelp(name, options);
					System.exit(0);
				} else {
					
					dispatcher.dispatchToAction(userAction, commandLine, configProps);
				}
			}
		}

	}
	
	private void printHelp( Options options, ActionDispatcher dispatcher )  {
		printHelp( options, dispatcher, null ) ;
	}
	
	/**
	 * Prints the main help from the Commons CLI and then prints each
	 * options customized help.
	 * 
	 * @param options
	 * @param dispatcher
	 */
	private void printHelp( Options options, ActionDispatcher dispatcher, String actionName )  {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(name, options);
		
		System.out.print( "\n" ) ;
		
		Map<Action, IAction> actionsMap = dispatcher.getRegisteredActions() ;
		
		if( actionName == null ) {
			System.out.println( "-------------------------------------------") ;
			System.out.println( "Examples of available Actions:" ) ;
			for( Action action : actionsMap.keySet() ) {
				IAction myAction = actionsMap.get( action ) ;
				System.out.println( "--action " + action.getName() ) ;
			}
			System.out.println( "\ntype --action validAction --help for more information" ) ;
		} else {
			IAction myAction = actionsMap.get( Action.findByName(actionName) ) ;
			myAction.printHelp() ;
			System.out.println( "\n" ) ;
			
		}

		
	}

}
