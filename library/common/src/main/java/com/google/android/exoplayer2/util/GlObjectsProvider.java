/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.exoplayer2.util;

import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import androidx.annotation.IntRange;
import androidx.annotation.RequiresApi;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.util.GlUtil.GlException;

// TODO(271433904): Expand this class to cover more methods in GlUtil.
/** Provider to customize the creation and maintenance of GL objects. */
public interface GlObjectsProvider {
  /**
   * Provider for GL objects that configures a GL context with 8-bit RGB or 10-bit RGB attributes,
   * and no depth buffer or render buffers.
   */
  GlObjectsProvider DEFAULT =
      new GlObjectsProvider() {
        @Override
        @RequiresApi(17)
        public EGLContext createEglContext(
            EGLDisplay eglDisplay, int openGlVersion, int[] configAttributes) throws GlException {
          return GlUtil.createEglContext(eglDisplay, openGlVersion, configAttributes);
        }

        @Override
        public GlTextureInfo createBuffersForTexture(int texId, int width, int height)
            throws GlException {
          int fboId = GlUtil.createFboForTexture(texId);
          return new GlTextureInfo(texId, fboId, /* rboId= */ C.INDEX_UNSET, width, height);
        }

        @Override
        public void clearOutputFrame() throws GlException {
          GlUtil.clearOutputFrame();
        }
      };

  /**
   * Creates a new {@link EGLContext} for the specified {@link EGLDisplay}.
   *
   * @param eglDisplay The {@link EGLDisplay} to create an {@link EGLContext} for.
   * @param openGlVersion The version of OpenGL ES to configure. Accepts either {@code 2}, for
   *     OpenGL ES 2.0, or {@code 3}, for OpenGL ES 3.0.
   * @param configAttributes The attributes to configure EGL with.
   * @throws GlException If an error occurs during creation.
   */
  @RequiresApi(17)
  EGLContext createEglContext(
      EGLDisplay eglDisplay, @IntRange(from = 2, to = 3) int openGlVersion, int[] configAttributes)
      throws GlException;

  /**
   * Returns a {@link GlTextureInfo} containing the identifiers of the newly created buffers.
   *
   * @param texId The identifier of the texture to attach to the buffers.
   * @param width The width of the texture in pixels.
   * @param height The height of the texture in pixels.
   * @throws GlException If an error occurs during creation.
   */
  GlTextureInfo createBuffersForTexture(int texId, int width, int height) throws GlException;

  /**
   * Clears the current render target.
   *
   * @throws GlException If an error occurs during clearing.
   */
  void clearOutputFrame() throws GlException;
}
