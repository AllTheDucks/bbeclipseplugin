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
 * Test rendering hook 'jsp.topFrame.start'
 * 
 * @author mmccullough
 * @since 9.2
 */
public class TopFrameRenderingHookTest implements RenderingHook
{

  @Override
  public String getContent()
  {
    JspResourceIncludeUtil.getThreadInstance().addCssFile( "/test/topFrameTest.css" );
    return "<div>jsp.topFrame.start</div>";
  }

  @Override
  public String getKey()
  {
    return "jsp.topFrame.start";
  }

}
