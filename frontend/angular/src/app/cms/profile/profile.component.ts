import { Component, OnInit } from '@angular/core';
import { UsersService, Profile} from 'src/app/admin/users/users.service';
import { FilesService } from 'src/app/_services/files.service';
import { NGXLogger } from 'ngx-logger';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  profile: Profile = {
    id: 0,
    profilePhoto: '',
    backgroundPhoto: '',
    description: '',
    posts: []
  };

  profiles = [];

  constructor(private logger: NGXLogger, private userService: UsersService, private filesService: FilesService) { }

  ngOnInit() {
    this.userService.getMyProfile().subscribe(a => {
      this.logger.debug(a);
      if (a.content[0]) { this.profiles = a.content; }
      this.logger.debug('ngOnInit::getMyProfile', a);
    });
  }

  deleteProfile(profile: Profile) {
    this.logger.debug('ProfileComponent::deleteProfile');
    this.userService.deleteProfile(profile).subscribe(
      r => this.ngOnInit()
    );
  }

  submitProfiePhoto(e, profile) {
    this.logger.debug('submitProfiePhoto', e);
    if (e.target.files && e.target.files[0]) {
      this.filesService.sendFile(e.target.files[0]).subscribe(r => {
        this.logger.debug('submitProfiePhoto', r);
        profile.profilePhoto = r.location;
        this.logger.debug(this.profile);
        this.submitProfile(profile);
      });
    }
  }

  submitBackgroundPhoto(e, profile: Profile) {
    this.logger.debug('submitBackgroundPhoto', e);
    if (e.target.files && e.target.files[0]) {
      this.filesService.sendFile(e.target.files[0]).subscribe(r => {
        this.logger.debug('submitBackgroundPhoto', r);
        profile.backgroundPhoto = r.location;
        this.logger.debug(this.profile);
        this.submitProfile(profile);
      });
    }
  }

  submitProfile(profile: Profile) {
    this.logger.debug('submitProfile', profile);
    this.userService.postProfile(profile).subscribe(r => {
      this.logger.debug('submitProfile', r);
      this.logger.debug('submitProfile', r);
      if (r != null) {
        this.ngOnInit();
        this.profile = {
          id: 0,
          profilePhoto: '',
          backgroundPhoto: '',
          description: '',
          posts: []
        };
      }
    });
  }

}
