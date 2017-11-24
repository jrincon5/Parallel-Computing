class IndexController < ApplicationController
  skip_before_filter :verify_authenticity_token
  $str=""
  def index
  end
  
  def word
    $str = params[:search]
    $amount = params[:amount]
    #print $str
    redirect_to index_pags_path
  end
  
  #def option
   # redirect_to index_index
  #end

end