import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FilesService } from 'src/app/_services/files.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-files',
  templateUrl: './files.component.html',
  styleUrls: ['./files.component.css']
})
export class FilesComponent implements OnInit {
  page: number = 0;
  @Input()
  images: [];
  envUrl: string;
  @Output()
  setFeaturedEventEmitter = new EventEmitter<any>();
  @Output()
  deleteImageEventEmitter = new EventEmitter<any>();

  constructor(private filesService: FilesService) { }

  ngOnInit() {
    this.envUrl = environment.apiUrl;
  }

  deleteImage(img: any) {
    this.filesService.deleteFile(img).subscribe(r =>
      this.deleteImageEventEmitter.emit(null)
    );
    console.log(this.images);
  }

  setAsFeatured(source: any) {
    console.log(JSON.stringify(source));
    this.setFeaturedEventEmitter.emit(source.url);
  }

}
