/******************************************************************************
 * Spine Runtimes Software License
 * Version 2.3
 * 
 * Copyright (c) 2013-2015, Esoteric Software
 * All rights reserved.
 * 
 * You are granted a perpetual, non-exclusive, non-sublicensable and
 * non-transferable license to use, install, execute and perform the Spine
 * Runtimes Software (the "Software") and derivative works solely for personal
 * or internal use. Without the written permission of Esoteric Software (see
 * Section 2 of the Spine Software License Agreement), you may not (a) modify,
 * translate, adapt or otherwise create derivative works, improvements of the
 * Software or develop new applications using the Software or (b) remove,
 * delete, alter or obscure any trademarks or any copyright, trademark, patent
 * or other intellectual property or proprietary rights notices on or in the
 * Software, including any copy thereof. Redistributions in binary or source
 * form must include this license and terms.
 * 
 * THIS SOFTWARE IS PROVIDED BY ESOTERIC SOFTWARE "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL ESOTERIC SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *****************************************************************************/

#ifndef SPINE_SKELETONRENDERER_H_
#define SPINE_SKELETONRENDERER_H_

#include <spine/spine.h>

namespace spine {

class PolygonBatch;

/** Draws a skeleton. */
class SkeletonRenderer {	// yeqing 2015-11-07
public:
	spSkeleton* skeleton;
	spBone* rootBone;
	float timeScale;
	bool debugSlots;
	bool debugBones;

	static SkeletonRenderer* createWithData (spSkeletonData* skeletonData, bool ownsSkeletonData = false);
	static SkeletonRenderer* createWithFile (const char* skeletonDataFile, spAtlas* atlas, float scale = 0);
	static SkeletonRenderer* createWithFile (const char* skeletonDataFile, const char* atlasFile, float scale = 0);

	SkeletonRenderer (spSkeletonData* skeletonData, bool ownsSkeletonData = false);
	SkeletonRenderer (const char* skeletonDataFile, spAtlas* atlas, float scale = 0);
	SkeletonRenderer (const char* skeletonDataFile, const char* atlasFile, float scale = 0);
	
	// yeqing 2015-11-07
	SkeletonRenderer(const char* skeletonRawData, int skeletonRawDataLen,
		const char* atlasRawData, int atlasRawDataLen, int* picHandle, int pngWidth, int pngHeight, float scale = 0);

	// yeqing 2015-11-07
	void setTimeScale(float scale);
	float getTimeScale() const;
	void setAlpha(int alpha);
	int getAlpha();
	void setPosition(float x, float y);
	float getPositionX();
	float getPositionY();
	void setScale(float scale);

	virtual ~SkeletonRenderer ();

	virtual void update (float deltaTime);
	virtual void draw(float drawAlpha = 1.f);								// yeqing 2015-11-07
	void boundingBox(float* left, float* top, float* right, float* bottom);	// yeqing 2015-11-07

	// --- Convenience methods for common Skeleton_* functions.
	void updateWorldTransform ();

	void setToSetupPose ();
	void setBonesToSetupPose ();
	void setSlotsToSetupPose ();

	/* Returns 0 if the bone was not found. */
	spBone* findBone (const char* boneName) const;
	/* Returns 0 if the slot was not found. */
	spSlot* findSlot (const char* slotName) const;
	
	/* Sets the skin used to look up attachments not found in the SkeletonData defaultSkin. Attachments from the new skin are
	 * attached if the corresponding attachment from the old skin was attached. If there was no old skin, each slot's setup mode
	 * attachment is attached from the new skin. Returns false if the skin was not found.
	 * @param skin May be 0.*/
	bool setSkin (const char* skinName);

	/* Returns 0 if the slot or attachment was not found. */
	spAttachment* getAttachment (const char* slotName, const char* attachmentName) const;
	/* Returns false if the slot or attachment was not found. */
	bool setAttachment (const char* slotName, const char* attachmentName);

protected:
	SkeletonRenderer ();
	void setSkeletonData (spSkeletonData* skeletonData, bool ownsSkeletonData);

	int getPicture (spRegionAttachment* attachment);		// yeqing 2015-11-07
	int getPicture (spMeshAttachment* attachment);			// yeqing 2015-11-07
	int getPicture (spSkinnedMeshAttachment* attachment);	// yeqing 2015-11-07

private:
	bool ownsSkeletonData;
	spAtlas* atlas;
	float* worldVertices;
	void initialize ();

	// yeqing 2015-11-07
	float fAlpha;
	float posX,posY;
	float scaleX, scaleY;
	float rotation;
};

}

#endif /* SPINE_SKELETONRENDERER_H_ */
