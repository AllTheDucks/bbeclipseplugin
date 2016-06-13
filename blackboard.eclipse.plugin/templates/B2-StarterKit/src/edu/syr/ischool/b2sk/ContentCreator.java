
package edu.syr.ischool.b2sk;

import blackboard.base.FormattedText;
import blackboard.data.ValidationException;
import blackboard.data.content.Content;
import blackboard.data.course.Course;
import blackboard.persist.*;
import blackboard.persist.content.ContentDbPersister;
import blackboard.platform.persistence.*;


public class ContentCreator {
	
	public static void createContent(String title, String inputHtml, String sCourseId, String sParentId) throws PersistenceException, ValidationException {
		
		// okay from here down
		// retrieve the Db persistence manager from the persistence service 
		BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager(); 
		// create a course document and set all desired attributes 
		Content content = new Content(); 
		content.setTitle( title ); 
		FormattedText text = new FormattedText( inputHtml , FormattedText.Type.HTML ); 
		content.setBody( text ); 
		content.setContentHandler( "resource/x-bb-document" ); 
		// ... set additional attributes ... 
		// these attributes of content require valid Ids... create and set them 
		Id courseId = bbPm.generateId( Course.DATA_TYPE, sCourseId ); 
		Id parentId = bbPm.generateId( Content.DATA_TYPE, sParentId ); 
		content.setCourseId( courseId ); 
		content.setParentId( parentId );       
		// retrieve the content persister and persist the content item     
		ContentDbPersister persister = (ContentDbPersister) bbPm.getPersister( ContentDbPersister.TYPE ); 
		persister.persist( content ); 
		
	
	}

}
