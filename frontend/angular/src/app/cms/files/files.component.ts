import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FilesService } from 'src/app/_services/files.service';
import { environment } from 'src/environments/environment';
import { ArticleModel } from 'src/app/_model/article.model';

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
  
  constructor() { }

  ngOnInit() {
    this.envUrl = environment.apiUrl;
  }
  

  setAsFeatured(source: any) {
    console.log(JSON.stringify(source));
    this.setFeaturedEventEmitter.emit(source.url);
  }

}
