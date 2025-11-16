/*-
 *  Copyright (C) 2010 Peter Baldwin   
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.peterbaldwin.client.android.vlcremote;

import android.net.Uri;
import junit.framework.TestCase;
import org.peterbaldwin.vlcremote.model.File;

public class VLCTest extends TestCase {
	public void testFileUri() {
		String input = "C\\\\Users\\\\Peter\\\\Music/Français";
		String mrl = File.getMrl(input, "mp3");

		assertTrue("MRL should start with file://", mrl.startsWith("file://"));

		String decoded = Uri.decode(mrl);
		// Be resilient to different encodings of Windows drive letters
		// Verify the decoded path ends with the expected Windows-style path
		assertTrue("Decoded MRL should end with Windows path",
				decoded.endsWith("C:/Users/Peter/Music/Français")
						|| decoded.endsWith("C:\\Users\\Peter\\Music/Français"));
	}
}
