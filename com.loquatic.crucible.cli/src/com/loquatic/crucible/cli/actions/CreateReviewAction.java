package com.loquatic.crucible.cli.actions;

import java.util.Properties ;

import org.apache.commons.cli.CommandLine ;
import org.apache.commons.cli.HelpFormatter ;
import org.apache.commons.cli.Options ;

import com.loquatic.crucible.cli.Action;
import com.loquatic.crucible.cli.CommandLineOption ;
import com.loquatic.crucible.json.IProtocolHandler ;
import com.loquatic.crucible.json.JsonHandler ;
import com.loquatic.crucible.json.ResponseData ;
import com.loquatic.crucible.rest.api.AddChangeset ;
import com.loquatic.crucible.rest.api.AddChangesetWrapper ;
import com.loquatic.crucible.rest.api.ChangesetData ;
import com.loquatic.crucible.rest.api.ReviewData ;
import com.loquatic.crucible.rest.api.State ;
import com.loquatic.crucible.rest.api.UserData ;
import com.loquatic.crucible.util.TargetUrlUtil ;




/**
 * Creates a review.
 * <p>
 * Arguments:
 * <ul>
 * <li>changeset - the commit # from the SCM</li>
 * <li>projectKey - the key as specified in Crucible</li>
 * <li>reviewName - the name/title of the review; defaults 
 *                                       review of changeSetNumber</li>
 * <li>description - required, a description of the review</li>
 * <li>allowOthersToJoin - optional, defaults to true</li>
 * </ul>
 * <p>
 * Requires an existing authToken to have been created.
 * 
 * 
 * @author jon.svede
 *
 */
public class CreateReviewAction extends AbstractAction {
	
	private Options myOptions ;
	
	

	public CreateReviewAction( IProtocolHandler myHandler ) {
		super( myHandler ) ;
		setAction( Action.CREATE_REVIEW ) ;
		myOptions = new Options() ;
		addOptions( myOptions ) ;
	}

	@Override
	public boolean perform(CommandLine commandLine, Properties props ) {
		
		boolean success = false ;
		
		ReviewData reviewData = createReviewData( commandLine ) ;
		
		String reviewId = createReview( props, reviewData ) ;
		
		if( reviewId == null ) {
			System.out.println( "Although it looks like the review was created, not able to get the reviewId; exiting." ) ;
			return false ;
		} else {
			success = true ;
		}
		
		
		return success ;
	}
	
	private String createReview( Properties props, ReviewData reviewData ) {
		
		String jsonString = null ;
		
		String reviewId = null ;
		try {
			jsonString = createJsonString( reviewData ) ;
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		jsonString = "{\"reviewData\":"+  jsonString + "}" ;
		
		StringBuilder url = TargetUrlUtil.createReviewUrl(  props ) ;
		
		appendAuthToken( url ) ;
		
		System.out.println( "[POST] targetUrl: " + url.toString() ) ;
		System.out.println( "payload: " + jsonString ) ;
		
		try {
			ResponseData response = getHandler().doPost( jsonString, url.toString() ) ;
			
			if( response.getHttpStatusCode() >= 200 && response.getHttpStatusCode() < 300 ) {
				// anything in the 200 range is a success.
				ReviewData review = ( ReviewData ) createObjectFromString( response.getResponseString(), new ReviewData() ) ;
				System.out.println( "Review created with ID: " + review.getPermaId().getId() ) ;
				reviewId = review.getPermaId().getId() ;
			}else {
				System.out.println( "there was an issue creating the review: " + response.getStatusLine() ) ;
			}
		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
		return reviewId ;
		
	}
	

	@Override
	public boolean addOptions(Options options) {

		options.addOption( CommandLineOption.PROJECT_KEY.getName(), 
				                 true, 
				                 "Required. The project key from Crucible " +
				                 "for this change." ) ;
		options.addOption( CommandLineOption.REVIEW_NAME.getName(), 
				                 true, 
				                 "Optional The name of this " +
				                 "review that will be shown in Crucible. Defaults " +
				                 "to 'Review for commit xxxx' where xxxx is the " +
				                 "changeset specfied.") ;
		options.addOption( CommandLineOption.DESCRIPTION.getName(), 
				                 true, 
				                 "Required. A description of the changeset being " +
				                 "reviewed." ) ;
		options.addOption( CommandLineOption.ALLOW_OTHERS_TO_JOIN.getName(), 
				                 true, 
				                 "Optional. Defaults to true." ) ;
		options.addOption( CommandLineOption.REPOSITORY.getName(), true, "The " +
				                 "name of the repository as defined in Crucible." ) ;
		return true;
	}

	@Override
	public void printHelp() {
		
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp( getActionName(), myOptions ) ;

		System.out.println( "\n\nExamples:" ) ;
		System.out.println( "-------------------------------------------------" ) ;
		System.out.println( "--action createReview --projectKey PROJ-ID " +
			 	            "--reviewName \"My Review of Foo\" --description \"Review of my changes to Foo.\"" +
				            "--repository myRepo --allowOtherToJoin true" ) ;
		
	} 
	
	private ReviewData createReviewData( CommandLine commandLine ) {
		String author = getUserName( commandLine );
		String reviewTitle = getReviewName( commandLine ) ;
		String description = getDescription( commandLine ) ;
		String project = getProjectKey( commandLine ) ;
		String allowJoinersStr = getAllowOthersToJoin( commandLine ) ;
		boolean allowJoiners = Boolean.parseBoolean( allowJoinersStr ) ;
		
		UserData user = createUserData( author )  ;
		ReviewData reviewData = new ReviewData() ;
		reviewData.setAuthor( user ) ;
		reviewData.setAllowReviewersToJoin( allowJoiners ) ;
		reviewData.setModerator( user ) ;
		reviewData.setDescription( description ) ;
		reviewData.setName( reviewTitle ) ;
		reviewData.setProjectKey( project ) ;
		// turns the review on, otherwise it's just a draft
		reviewData.setState( State.Review ) ;
		
		return reviewData ;
	}
	
	private UserData createUserData( String name ) {
		UserData userData = new UserData() ;
		userData.setUserName( name ) ;
		
		return userData ;
	}

 
	

}
