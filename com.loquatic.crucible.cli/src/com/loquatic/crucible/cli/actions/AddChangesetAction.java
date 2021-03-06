package com.loquatic.crucible.cli.actions;

import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.loquatic.crucible.cli.Action;
import com.loquatic.crucible.cli.CommandLineOption;
import com.loquatic.crucible.json.IProtocolHandler;
import com.loquatic.crucible.json.ResponseData;
import com.loquatic.crucible.rest.api.AddChangeset;
import com.loquatic.crucible.rest.api.AddChangesetWrapper;
import com.loquatic.crucible.rest.api.ChangesetData;
import com.loquatic.crucible.util.TargetUrlUtil;

public class AddChangesetAction extends AbstractAction {
	
	public AddChangesetAction( IProtocolHandler myHandler ) {
		super( myHandler ) ;
		setAction( Action.ADD_CHANGESETS_TO_REVIEW ) ;
	}

	@Override
	public boolean perform(CommandLine commandLine, Properties props) {
		boolean success = false ;
	
		String reviewId = getReviewId( commandLine ) ;
		String changeSet = getChangeSet( commandLine ) ;
		String repoName = getRepoName( commandLine ) ;
		
		String[] changesetIds = changeSet.split(",");
		System.out.println( "Found " + changesetIds.length + " review ids" ) ;
		for( String changesetIdForRvw : changesetIds ) {
			System.out.println( "Add changeset " + changesetIdForRvw + " to the review " + reviewId ) ;
			success = addChangeSetToReview( props, reviewId, changesetIdForRvw, repoName ) ;
			if( !success ) {
				return false ;
			}
		}
		return success ;
	}

	@Override
	public boolean addOptions(Options options) {
		options.addOption( CommandLineOption.CHANGESET.getName(), 
                true, 
                "Required. A comma separated list of change ids from the SCM for this " +
                "Crucible project that this review will reference." ) ;
		options.addOption( CommandLineOption.REPOSITORY.getName(), true, "The " +
                "name of the repository as defined in Crucible." ) ;
		options.addOption( CommandLineOption.REVIEW_ID.getName(), true, "The ID of the review you wish to close." ) ;


		return true ;
	}

	@Override
	public void printHelp() {
		System.out.println( "--action addChangeset --reviewId PROJ-ID --changeset 99999 --repository repoName" ) ;
		System.out.println( "--action addChangeset --reviewId PROJ-ID --changeset 99999,88888,77777 --repository repoName" ) ;
	}
	
	public boolean addChangeSetToReview( Properties props, String reviewId, String changeSet, String repoName ) {
		AddChangesetWrapper changeSetWrapper = createChangesetData( changeSet, repoName ) ;
		boolean success = false ;
		
		StringBuilder url = TargetUrlUtil.createReviewUrl(  props ) ;
		
		//TODO: fix this, it's horrible
		url.append( reviewId ).append("/addChangeset").append( "?" ).append("FEAUTH=").append( getToken() ) ;
		
		System.out.println( "URL: " + url.toString() ) ;
		
		try {
			String jsonChangeset = createJsonString( changeSetWrapper ) ;
			
			System.out.println( "Changeset request: " + jsonChangeset ) ;
			
			ResponseData response  = getHandler().doPost( jsonChangeset, url.toString() ) ;
			
			if( response.getHttpStatusCode() >= 200 && response.getHttpStatusCode() < 300 ) {
				System.out.println( "Successfully added changeset " + changeSet + 
						                " to review " + reviewId + " in repo " + repoName  ) ;
				System.out.println( "status: " + response.getStatusLine() + "\n\tresponse data: " + response.getResponseString() ) ;
				success = true ;
			} else {
				System.out.println( "Unable to add changeset(s) " + changeSet + " to review " + reviewId ) ;
				System.out.println( "System returned: " + response.getStatusLine() ) ;
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		
		return success ;
	}
	
	public  AddChangesetWrapper createChangesetData( String revision, String repoName ) {
		
		AddChangeset addChangeset = new AddChangeset() ;
		
		AddChangesetWrapper wrapper = new AddChangesetWrapper() ;

		String[] changeSets = revision.split(",") ;
		
		for ( String aRevision : changeSets ) {
			ChangesetData changesetData = new ChangesetData();
			changesetData.setId(aRevision);
			addChangeset.addChangesetData(changesetData);
			wrapper.setAddChangeset( addChangeset ) ;
		}
		
		wrapper.setRepository( repoName ) ;
		
		return wrapper ;
	}



}
