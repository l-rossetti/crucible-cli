/*
 * Copyright (c) 2013, Loquatic Software, LLC
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, 
 *      this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright 
 *      notice, this list of conditions and the following disclaimer in the 
 *      documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */
package com.loquatic.crucible.cli.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter ;
import org.apache.commons.cli.Options ;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.loquatic.crucible.cli.Action;
import com.loquatic.crucible.cli.CommandLineOption;
import com.loquatic.crucible.json.IProtocolHandler;

public abstract class AbstractAction implements IAction {
	
	private Action action ;
	private String token ;
	private String tokenFileName ;
	private File tokenFile ;
	
	protected Options myOptions ;
	
	protected IProtocolHandler handler ;
	
	public AbstractAction() {
		String userHomeDir = System.getProperty( "user.home" ) ;
		String tknFileName = "crucible-cli.tkn" ;

		StringBuilder fileNameBuilder = new StringBuilder() ;
		fileNameBuilder.append( userHomeDir ).append( File.separator).append( tknFileName ) ;
		
		tokenFileName = fileNameBuilder.toString() ;
		
		tokenFile = new File( tokenFileName ) ;
		
		if( tokenExists() ) {
			loadToken() ;
		}
		
		myOptions = new Options() ;
		
		addOptions( myOptions ) ;
	}
	
	public AbstractAction( IProtocolHandler myHandler ) {
		this() ;
		setHandler( myHandler ) ;
		//myOptions = new Options() ;
	}
	
	
	public void setHandler( IProtocolHandler myHandler ) {
		handler = myHandler ;
	}
	
	public IProtocolHandler getHandler() {
		return handler ;
	}

	protected boolean tokenExists() {
		if( tokenFile.exists() && tokenFile.canRead() ) {
			return true ;
		}
		return false ;
	}
	
	protected void appendAuthToken( StringBuilder url ) {
		url.append( "?" ).append("FEAUTH=").append( getToken() ) ;
	}
	
	protected void loadToken() {
		
		try {
			BufferedReader bfrdFileReader = new BufferedReader( new FileReader( tokenFile ) ) ;
			
			// the file should only have the one line!
			token = bfrdFileReader.readLine() ;
			
			//System.out.println( "token loaded: " + token + " from file " + tokenFile ) ;
			
			bfrdFileReader.close() ;
		} catch ( FileNotFoundException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected String getToken() {
		if( token == null ) {
			loadToken() ;
		}
		return token ;
	}
	
	protected String createJsonString(Object objToConvert) throws Exception {
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
		String output = mapper.writeValueAsString(objToConvert);

		return output;
	}
	
	protected Object createObjectFromString( String json, Object target ) {
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

		try {
			target = mapper.readValue( json, target.getClass() ) ;
		} catch ( JsonParseException e ) {
			e.printStackTrace();
		} catch ( JsonMappingException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
		return target ;
	}
	
	/**
	 * The main main help entry point. Calls each subclasses 
	 * getHelOverview() and getHelpExamples() methods. Provides
	 * a fairly consistent way of formatting and presenting 
	 * help to end users.
	 */
	@Override
	public void printHelp() {
		
		System.out.println( getHelpOverview() ) ;
		
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp( getActionName(), myOptions ) ;

		System.out.println( "\nExamples:" ) ;
		System.out.println( "-------------------------------------------------" ) ;

		System.out.println( getHelpExamples() ) ;
	} 
	
	
	
	@Override
	public Options getOptions() {
		return myOptions ;
	}

	@Override
	public String getHelpOverview() {
		return "OOPS! My author hasn't finished me yet! Missing Help Overview!" ;
	}

	@Override
	public String getHelpExamples() {
		return "D'OH! My author hasn't finished me yet! Missing Help Examples!" ; 
	}
	
	public String getActionName() {
		return action.getName() ;
	}
	
	protected Action getAction() {
		return action;
	}

	protected void setAction(Action action) {
		this.action = action;
	}

	protected String getAllowOthersToJoin( CommandLine cmdLine ) {
		return getCommandLineOption( cmdLine, CommandLineOption.ALLOW_OTHERS_TO_JOIN ) ;
	}
	
	protected String getProjectKey( CommandLine cmdLine ) {
		return getCommandLineOption( cmdLine, CommandLineOption.PROJECT_KEY ) ;
	}
	
	protected String getDescription( CommandLine cmdLine ) {
		return getCommandLineOption( cmdLine, CommandLineOption.DESCRIPTION ) ;
	}
	
	protected String getReviewName( CommandLine cmdLine ) {
		return getCommandLineOption( cmdLine, CommandLineOption.REVIEW_NAME ) ;
	}
	
	protected String getReviewers( CommandLine cmdLine ) {
		return getCommandLineOption( cmdLine, CommandLineOption.REVIEWERS ) ;
	}
	
	protected String getReviewId( CommandLine cmdLine ) {
		return getCommandLineOption( cmdLine, CommandLineOption.REVIEW_ID ) ;
	}
	
	protected String getChangeSet( CommandLine cmdLine ) {
		return getCommandLineOption( cmdLine, CommandLineOption.CHANGESET ) ;
	}
	
	protected String getRepoName( CommandLine cmdLine ) {
		return getCommandLineOption( cmdLine, CommandLineOption.REPOSITORY ) ;
	}
	
	protected String getUserName( CommandLine cmdLine ) {
		return getCommandLineOption( cmdLine, CommandLineOption.USERNAME ) ;
	}
	
	protected String getCommandLineOption( CommandLine commandLine, CommandLineOption option ) {
		
		return commandLine.getOptionValue( option.getName() ) ;
		
	}

}
