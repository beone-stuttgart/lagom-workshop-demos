package controllers.model;

import com.beone.lagom.contribute.api.ContributionInfo;
import com.beone.lagom.organize.api.models.PresentAdminInfo;
import com.beone.lagom.wishlist.api.WishlistResponse;

import javax.annotation.concurrent.Immutable;

/**
 * collects edit data.
 */
@Immutable
public class EditData {
  public final PresentAdminInfo present;
  public final WishlistResponse wishlist;
  public final ContributionInfo contributions;

  public EditData(final PresentAdminInfo present, final WishlistResponse wishlist, final ContributionInfo contributions) {
    this.present = present;
    this.wishlist = wishlist;
    this.contributions = contributions;
  }
}
