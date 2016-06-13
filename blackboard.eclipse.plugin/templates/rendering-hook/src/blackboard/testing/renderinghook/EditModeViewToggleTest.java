/*
 * (C) Copyright Blackboard Inc. 1998-2010 - All Rights Reserved
 * 
 * Permission to use, copy, modify, and distribute this software without prior explicit written approval is strictly prohibited. Please refer to the
 * file "copyright.html" for further important copyright and licensing information.
 * 
 * BLACKBOARD MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. BLACKBOARD SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package blackboard.testing.renderinghook;

import blackboard.platform.servlet.JspResourceIncludeUtil;
import blackboard.servlet.renderinghook.RenderingHook;

/**
 * Test rendering hook 'tag.editModeViewToggle.start'
 * 
 * @author mmccullough
 * @since 9.2
 */
public class EditModeViewToggleTest implements RenderingHook
{

  @Override
  public String getContent()
  {
    JspResourceIncludeUtil.getThreadInstance().addCssFile( "/test/editModeViewToggleTest.css" );
    return "<div>tag.editModeViewToggle.start</div>";
  }

  @Override
  public String getKey()
  {
    return "tag.editModeViewToggle.start";
  }

}
